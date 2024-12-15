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
    ROBOT('@');

    companion object {
        fun of(c: Char): Tile {
            return entries.single { it.symbol == c }
        }
    }
}

fun main() {
    puzzle(10092) { lines ->
        val warehouse = lines
            .filter { it.startsWith("#") }
            .map { it.map { Tile.of(it) }.toMutableList() }
            .toMutableList()
        val moves = lines
            .filterNot { it.startsWith("#") }
            .flatMap { it.map { Direction.of(it) } }
        var robot = findRobot(warehouse)
        moves.forEach { move ->
            robot = attemptMove(move, robot, warehouse)
        }
        warehouse.flatMapIndexed { y: Int, row: MutableList<Tile> ->
            row.mapIndexed { x: Int, tile: Tile ->
                if (tile == Tile.BOX) Point(x, y).gps() else 0
            }
        }.sum()
    }
}

fun attemptMove(
    move: Direction,
    robot: Point,
    warehouse: MutableList<MutableList<Tile>>
): Point {
    val next = move.move(robot)
    freeSpaceAhead(move, next, warehouse) ?: return robot

    pushBox(move, next, warehouse)
    warehouse[robot.y][robot.x] = Tile.EMPTY
    warehouse[next.y][next.x] = Tile.ROBOT
    return next
}

fun pushBox(move: Direction, point: Point, warehouse: MutableList<MutableList<Tile>>) {
    val tile = warehouse[point.y][point.x]
    if (tile != Tile.BOX) return

    val next = move.move(point)
    pushBox(move, next, warehouse)

    warehouse[point.y][point.x] = Tile.EMPTY
    warehouse[next.y][next.x] = Tile.BOX
}

fun freeSpaceAhead(
    move: Direction,
    point: Point,
    warehouse: MutableList<MutableList<Tile>>
): Point? {
    val tile = warehouse[point.y][point.x]
    if (tile == Tile.WALL) return null
    if (tile == Tile.EMPTY) return point

    val next = move.move(point)
    return freeSpaceAhead(move, next, warehouse)
}

fun findRobot(warehouse: MutableList<MutableList<Tile>>): Point {
    warehouse.indexOfFirst { it.contains(Tile.ROBOT) }.let { y ->
        return Point(warehouse[y].indexOf(Tile.ROBOT), y)
    }
}
