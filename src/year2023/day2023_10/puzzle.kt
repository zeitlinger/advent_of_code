package year2023.day2023_10

import puzzle
import year2023.day2023_10.Connector.NONE
import year2023.day2023_10.Connector.START

data class Point(val x: Int, val y: Int)

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}

enum class Connector(val symbol: String, val directions: List<Direction>) {
    VERTICAL("|", listOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL("-", listOf(Direction.LEFT, Direction.RIGHT)),
    UP_RIGHT("L", listOf(Direction.UP, Direction.RIGHT)),
    DOWN_RIGHT("F", listOf(Direction.DOWN, Direction.RIGHT)),
    DOWN_LEFT("7", listOf(Direction.DOWN, Direction.LEFT)),
    UP_LEFT("J", listOf(Direction.UP, Direction.LEFT)),
    NONE(".", emptyList()),
    START("S", emptyList()), ;
}

val connectorMap = Connector.entries.associateBy { it.symbol }

fun main() {
    puzzle(8) { lines ->
        val field = lines.map { it.map { connectorMap.getValue(it.toString()) } }
        val start = lines.indices.flatMapIndexed { y, _ ->
            lines[y].indices.mapNotNull { x ->
                val c = field[y][x]
                if (c == START) {
                    Point(x, y)
                } else {
                    null
                }
            }
        }.first()
        val first = destinations(field, start)
        if (first.size != 2) {
            throw IllegalArgumentException("Invalid start")
        }
        val loop = mutableListOf(start)
        var current = first.first()
        while (current != start) {
            loop.add(current)
            if (current == first.last()) {
                break
            }
            val destinations = destinations(field, current)
            val next = destinations.first { it !in loop }
            current = next
        }
        loop.add(start)
        loop.size / 2
    }
}

fun destinations(field: List<List<Connector>>, start: Point): List<Point> {
    val out = when (val connector = field[start.y][start.x]) {
        NONE -> throw IllegalArgumentException("Invalid start")
        START -> Direction.entries
        else -> connector.directions
    }
    return out.mapNotNull {
        val x = start.x + it.x
        val y = start.y + it.y
        val inField = x in field[0].indices && y in field.indices
        if (inField && field[y][x].directions.contains(it.opposite())) Point(x, y) else null
    }
}
