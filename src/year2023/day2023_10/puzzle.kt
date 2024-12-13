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

data class Search(
    val loop: List<Point>,
    val field: List<List<Connector>>,
    val outside: MutableSet<Point>,
    val enclosed: MutableSet<Point>)

fun lookRight(connector: Connector, from: Direction): List<Direction> {
    return when (connector) {
        Connector.VERTICAL -> when (from) {
            Direction.UP -> listOf(Direction.LEFT)
            Direction.DOWN -> listOf(Direction.RIGHT)
            else -> throw IllegalArgumentException("Invalid direction")
        }

        Connector.HORIZONTAL -> when (from) {
            Direction.LEFT -> listOf(Direction.DOWN)
            Direction.RIGHT -> listOf(Direction.UP)
            else -> throw IllegalArgumentException("Invalid direction")
        }

        Connector.UP_LEFT -> when (from) {
            Direction.UP -> listOf()
            Direction.LEFT -> listOf(Direction.RIGHT, Direction.DOWN)
            else -> throw IllegalArgumentException("Invalid direction")
        }

        Connector.UP_RIGHT -> when (from) {
            Direction.UP -> listOf(Direction.LEFT, Direction.DOWN)
            Direction.RIGHT -> listOf()
            else -> throw IllegalArgumentException("Invalid direction")
        }

        Connector.DOWN_LEFT -> when (from) {
            Direction.DOWN -> listOf(Direction.RIGHT, Direction.UP)
            Direction.LEFT -> listOf()
            else -> throw IllegalArgumentException("Invalid direction")
        }

        Connector.DOWN_RIGHT -> when (from) {
            Direction.DOWN -> listOf()
            Direction.RIGHT -> listOf(Direction.LEFT, Direction.UP)
            else -> throw IllegalArgumentException("Invalid direction")
        }

        else -> throw IllegalArgumentException("Invalid connector")
    }
}

fun lookLeft(connector: Connector, from: Direction): List<Direction> {
    return lookRight(connector, connector.directions.single { it != from })
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
    val search = Search(loop, field, outside, enclosed)

    visitLoop(search, ::lookLeft)

    if (outside.isNotEmpty()) {
        outside.clear()
        enclosed.clear()
        visitLoop(search, ::lookRight)

        if (outside.isNotEmpty()) {
            throw IllegalArgumentException("Invalid loop")
        }
    }

    print(search)

    return enclosed.size.toLong()
}

private fun print(
    search: Search,
    highlight: List<Point> = emptyList(),
) {
    val l = search.field.mapIndexed { y, row ->
        row.mapIndexed { x, connector ->
            val p = Point(x, y)
            when (p) {
                in highlight -> "#"
                in search.enclosed -> "*"
                in search.outside -> " "
                in search.loop -> connector.symbol
                else -> " "
            }
        }
    }
    l.forEach { println(it.joinToString("")) }
}

private fun visitLoop(
    search: Search,
    takeSide: (Connector, Direction) -> List<Direction>,
) {
    search.loop.drop(1).forEachIndexed { index, point ->
        val last = search.loop[index]
        val connector = search.field[point.y][point.x]
        takeSide(connector, Direction.entries.single { point.move(it) == last }).forEach { d ->
            val area = mutableSetOf<Point>()
            grow(point.move(d), area, search, listOf(point))
            if (search.outside.isNotEmpty()) {
                return
            }
            search.enclosed.addAll(area)
        }
    }
}

fun grow(
    point: Point,
    area: MutableSet<Point>,
    search: Search,
    highlight: List<Point>,
) {
//    println("Grow $point")
    if (point in search.outside || point in search.enclosed) {
        area.clear()
        return
    }
    if (point in area || point in search.loop) {
        return
    }
//    println("Add $point")
//    print(search, highlight)
    area.add(point)
    if (point.x !in search.field[0].indices || point.y !in search.field.indices) {
//        println("Outside")
        search.outside.addAll(area)
        area.clear()
        return
    }

    Direction.entries.forEach { direction ->
        grow(point.move(direction), area, search, highlight + point)
        if (area.isEmpty()) {
            return
        }
    }
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
    loop.add(start)
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
