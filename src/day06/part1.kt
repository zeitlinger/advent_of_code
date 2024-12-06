package day06

import puzzle
import kotlin.math.absoluteValue

data class Point(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    fun turnRight(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun ahead(p: Point): Point = Point(p.x + dx, p.y + dy)
}

fun main() {
    puzzle(41) { lines ->
        var pos = getStart(lines)
        var direction = Direction.NORTH
        val maze = lines.map { it.replace('^', '.').toCharArray().toMutableList() }.toMutableList()

        fun isWall(p: Point): Boolean {
            return maze[p.y][p.x] == '#'
        }

        fun isInside(p: Point): Boolean {
            return p.y in maze.indices && p.x in maze[p.y].indices
        }

        fun mark(p: Point) {
            maze[p.y][p.x] = 'X'
        }

        fun countVisited(): Int {
            return maze.sumOf { it.count { it == 'X' } }
        }

        fun print() {
            println("Position: $pos")
            println(maze.joinToString("\n") { it.joinToString("") })
        }

        while (true) {
            mark(pos)
            val next = direction.ahead(pos)
            if (!isInside(next)) {
                break
            }

            if (isWall(next)) {
                direction = direction.turnRight()
            } else {
                pos = next
            }
        }

        print()

        countVisited()
    }
}

private fun getStart(lines: List<String>): Point {
    for (row in lines.indices) {
        for (col in lines[row].indices) {
            if (lines[row][col] == '^') {
                return Point(col, row)
            }
        }
    }
    throw IllegalArgumentException("No start found")
}
