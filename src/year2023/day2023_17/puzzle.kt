package year2023.day2023_17

import Direction
import Point
import puzzle

import java.lang.Math.abs

typealias Barrier = Set<Point>

const val MAX_SCORE = 99999999

abstract class Grid(private val cost: List<List<Int>>) {

    open fun heuristicDistance(start: Point, finish: Point): Int {
        val dx = abs(start.x - finish.x)
        val dy = abs(start.y - finish.y)
        return (dx + dy) + (-2) * minOf(dx, dy)
    }

    abstract fun getNeighbours(position: Point, path: List<Point>): List<Point>

    open fun moveCost(from: Point, to: Point) = cost[to.y][to.x]
}

class SquareGrid(width: Int, height: Int, cost: List<List<Int>>) : Grid(cost) {
    private val heightRange: IntRange = (0 until height)
    private val widthRange: IntRange = (0 until width)

    override fun getNeighbours(position: Point, path: List<Point>): List<Point> = Direction.entries
        .mapNotNull {
            if (path.size > 3) {
                val list = path.subList(path.size - 3, path.size).windowed(2).map {
                    it[0].directionTo(it[1])
                }
                if (list.all { it == list[0] }) {
                    return@mapNotNull null
                }
            }
            position.move(it)
        }
        .filter { inGrid(it) }

    private fun inGrid(it: Point) = (it.x in widthRange) && (it.y in heightRange)
}


/**
 * Implementation of the A* Search Algorithm to find the optimum path between 2 points on a grid.
 *
 * The Grid contains the details of the barriers and methods which supply the neighboring vertices and the
 * cost of movement between 2 cells.  Examples use a standard Grid which allows movement in 8 directions
 * (i.e. includes diagonals) but alternative implementation of Grid can be supplied.
 *
 */
fun aStarSearch(start: Point, finish: Point, grid: Grid): Pair<List<Point>, Int> {

    /**
     * Use the cameFrom values to Backtrack to the start position to generate the path
     */
    fun generatePath(currentPos: Point, cameFrom: Map<Point, Point>): List<Point> {
        val path = mutableListOf(currentPos)
        var current = currentPos
        while (cameFrom.containsKey(current)) {
            current = cameFrom.getValue(current)
            path.add(0, current)
        }
        return path.toList()
    }

    val openVertices = mutableSetOf(start)
    val closedVertices = mutableSetOf<Point>()
    val costFromStart = mutableMapOf(start to 0)
    val estimatedTotalCost = mutableMapOf(start to grid.heuristicDistance(start, finish))

    val cameFrom = mutableMapOf<Point, Point>()  // Used to generate path by back tracking

    while (openVertices.size > 0) {

        val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }

        val path = generatePath(currentPos, cameFrom)

        // Check if we have reached the finish
        if (currentPos == finish) {
            // Backtrack to generate the most efficient path
            return Pair(path, estimatedTotalCost.getValue(finish)) // First Route to finish will be optimum route
        }

        // Mark the current vertex as closed
        openVertices.remove(currentPos)
        closedVertices.add(currentPos)

        grid.getNeighbours(currentPos, path)
            .filterNot { closedVertices.contains(it) }  // Exclude previous visited vertices
            .forEach { neighbour ->
                val score = costFromStart.getValue(currentPos) + grid.moveCost(currentPos, neighbour)
                if (score < costFromStart.getOrDefault(neighbour, MAX_SCORE)) {
                    if (!openVertices.contains(neighbour)) {
                        openVertices.add(neighbour)
                    }
                    cameFrom.put(neighbour, currentPos)
                    costFromStart.put(neighbour, score)
                    estimatedTotalCost.put(neighbour, score + grid.heuristicDistance(neighbour, finish))
                }
            }

    }

    throw IllegalArgumentException("No Path from Start $start to Finish $finish")
}

fun main() {
    puzzle(102) { lines ->
        val costMap = lines.map { it.map { it.digitToInt() } }

        val (path, cost) = aStarSearch(Point(0, 0), Point(7, 7), SquareGrid(8, 8, costMap))

        for (y in 0..7) {
            for (x in 0..7) {
                val point = Point(x, y)
                when (point) {
//                    in blocked -> {
//                        print('#')
//                    }
                    in path -> {
                        print('x')
                    }
                    else -> print('.')
                }
            }
            println()
        }

        println("Cost: $cost  Path: $path")
        cost
    }
}
