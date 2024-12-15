package year2024.day2024_15

import puzzle

data class Point(val x: Int, val y: Int) {
    fun gps(): Int = y * 100 + x
}

enum class Direction(val x: Int, val y: Int, val symbol: Char) {
    UP(0, -1, '^'),
    DOWN(0, 1, 'v'),
    LEFT(-1, 0, '<'),
    RIGHT(1, 0, '>');

    fun move(p: Point): Point {
        return Point(p.x + x, p.y + y)
    }

    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }

    companion object {
        fun of(c: Char): Direction {
            return entries.single { it.symbol == c }
        }
    }
}

enum class Tile(val symbol: Char) {
    WALL('#'),
    EMPTY('.'),
    BOX('O'),
    BOX_LEFT('['),
    BOX_RIGHT(']'),
    ROBOT('@');

    companion object {
        fun of(c: Char): Tile {
            return entries.single { it.symbol == c }
        }
    }
}

fun main() {
    puzzle(9021) { lines ->
        val warehouse = lines
            .filter { it.startsWith("#") }
            .map {
                it
                    .flatMap {
                        when (it) {
                            '#' -> "##"
                            'O' -> "[]"
                            '@' -> "@."
                            '.' -> ".."
                            else -> throw IllegalArgumentException("Invalid character")
                        }.toCharArray().toList()
                    }
                    .map { Tile.of(it) }
                    .toMutableList()
            }
            .toMutableList()
        val moves = lines
            .filterNot { it.startsWith("#") }
            .flatMap { it.map { Direction.of(it) } }
        var robot = findRobot(warehouse)
        printWarehouse(warehouse)
        moves.forEachIndexed { index, move ->
            robot = attemptMove(move, robot, warehouse)
            println("Move: $move, Index: $index")
            printWarehouse(warehouse)
            assertConsistency(warehouse)
        }
        printWarehouse(warehouse)
        warehouse.flatMapIndexed { y: Int, row: MutableList<Tile> ->
            row.mapIndexed { x: Int, tile: Tile ->
                if (tile == Tile.BOX_LEFT) Point(x, y).gps() else 0
            }
        }.sum()
    }
}

fun assertConsistency(warehouse: MutableList<MutableList<Tile>>) {
    warehouse.forEach { row ->
        row.forEachIndexed { index, tile ->
            if (tile == Tile.BOX_LEFT) {
                if (row[index + 1] != Tile.BOX_RIGHT) {
                    throw IllegalStateException("Box left without box right")
                }
            }
            if (tile == Tile.BOX_RIGHT) {
                if (row[index - 1] != Tile.BOX_LEFT) {
                    throw IllegalStateException("Box right without box left")
                }
            }
        }
    }
}

fun printWarehouse(warehouse: MutableList<MutableList<Tile>>) {
    warehouse.forEach { row ->
        println(row.joinToString("") { it.symbol.toString() })
    }
}

fun attemptMove(
    move: Direction,
    robot: Point,
    warehouse: MutableList<MutableList<Tile>>
): Point {
    val next = move.move(robot)
    freeSpaceAhead(move, listOf(next), warehouse) ?: return robot

    pushBox(move, listOf(next), warehouse)
    warehouse[robot.y][robot.x] = Tile.EMPTY
    warehouse[next.y][next.x] = Tile.ROBOT
    return next
}

fun pushBox(direction: Direction, points: List<Point>, warehouse: MutableList<MutableList<Tile>>) {
    val tiles = points.map { warehouse[it.y][it.x] }
    if (tiles.all { it == Tile.EMPTY }) return

    val next = nextPoints(points, direction, tiles)
    pushBox(direction, next, warehouse)

    next.forEach { point ->
        val from = direction.opposite().move(point)
        val to = direction.move(from)
        warehouse[to.y][to.x] = warehouse[from.y][from.x]
        warehouse[from.y][from.x] = Tile.EMPTY
    }
}

fun freeSpaceAhead(
    direction: Direction,
    points: List<Point>,
    warehouse: MutableList<MutableList<Tile>>
): List<Point>? {
    val tiles = points.map { warehouse[it.y][it.x] }
    if (tiles.any { it == Tile.WALL }) return null
    if (tiles.all { it == Tile.EMPTY }) return points

    val next = nextPoints(points, direction, tiles)

    return freeSpaceAhead(direction, next, warehouse)
}

private fun nextPoints(
    points: List<Point>,
    direction: Direction,
    tiles: List<Tile>
): List<Point> {
    val next = points.map { direction.move(it) }.toMutableList()
    if (direction == Direction.UP) {
        if (tiles.first() == Tile.BOX_RIGHT) {
            next.add(0, Direction.LEFT.move(next.first()))
        }
        if (tiles.last() == Tile.BOX_LEFT) {
            next.add(Direction.RIGHT.move(next.last()))
        }
    }
    if (direction == Direction.DOWN) {
        if (tiles.first() == Tile.BOX_LEFT) {
            next.add(0, Direction.RIGHT.move(next.first()))
        }
        if (tiles.last() == Tile.BOX_RIGHT) {
            next.add(Direction.LEFT.move(next.last()))
        }
    }
    return next
}

fun findRobot(warehouse: MutableList<MutableList<Tile>>): Point {
    warehouse.indexOfFirst { it.contains(Tile.ROBOT) }.let { y ->
        return Point(warehouse[y].indexOf(Tile.ROBOT), y)
    }
}
