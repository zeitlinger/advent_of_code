package year2023.day2023_10

import puzzle
import year2023.day2023_10.Connector.NONE
import year2023.day2023_10.Connector.START

data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction): Point {
        return Point(x + direction.x, y + direction.y)
    }
}

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
    puzzle(10) { lines ->
        val field = lines.map { it.map { connectorMap.getValue(it.toString()) }.toMutableList() }.toMutableList()
        val loop = findLoop(field)
        enclosedArea(field, loop)
    }
}

fun enclosedArea(field: List<List<Connector>>, loop: List<Point>): Long {
    val outside = mutableSetOf<Point>()
    val enclosed = mutableSetOf<Point>()

    fun grow(point: Point, area: MutableSet<Point>) {
        if (point in outside || point in enclosed) {
            area.clear()
            return
        }
        if (point in area || point in loop) {
            return
        }
        area.add(point)
        if (point.x !in field[0].indices || point.y !in field.indices) {
            outside.addAll(area)
            area.clear()
            return
        }

        Direction.entries.forEach { direction ->
            grow(point.move(direction), area)
            if (area.isEmpty()) {
                return
            }
        }
    }

    loop.forEach { point ->
        val look = directions(field, point).map { it.opposite() }
        look.forEach { direction ->
            val area = mutableSetOf<Point>()
            point.move(direction).let { grow(it, area) }
            enclosed.addAll(area)
        }
    }
    val l = field.mapIndexed { y, row ->
        row.mapIndexed { x, connector ->
            val p = Point(x, y)
            when (p) {
                in enclosed -> "I"
                in outside -> "O"
                in loop -> connector.symbol
                else -> " "
            }
        }
    }
    l.forEach { println(it.joinToString("")) }

    return enclosed.size.toLong()
}

private fun findLoop(
    field: MutableList<MutableList<Connector>>
): List<Point> {
    val start = field.indices.flatMapIndexed { y, _ ->
        field[y].indices.mapNotNull { x ->
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
    val directions = directions(field, start).toSet()
    val firstConnector = Connector.entries.single { it.directions.toSet() == directions }
    field[start.y][start.x] = firstConnector
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
    return loop
}

fun directions(field: List<List<Connector>>, start: Point): List<Direction> {
    val out = when (val connector = field[start.y][start.x]) {
        NONE -> throw IllegalArgumentException("Invalid start")
        START -> Direction.entries
        else -> connector.directions
    }
    return out.mapNotNull {
        val x = start.x + it.x
        val y = start.y + it.y
        val inField = x in field[0].indices && y in field.indices
        if (inField && field[y][x].directions.contains(it.opposite())) it else null
    }
}

fun destinations(field: List<List<Connector>>, start: Point): List<Point> {
    return directions(field, start).map { start.move(it) }
}
