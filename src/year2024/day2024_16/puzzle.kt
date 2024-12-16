package year2024.day2024_16

import Direction
import Point
import puzzle

enum class Tile(val symbol: Char) {
    WALL('#'),
    EMPTY('.'),
    START('S'),
    END('E');

    companion object {
        fun of(c: Char): Tile {
            return entries.single { it.symbol == c }
        }
    }
}

data class Maze(val tiles: List<List<Tile>>) {
    fun print() {
        tiles.forEach { row ->
            row.forEach { tile ->
                print(tile.symbol)
            }
            println()
        }
    }

    fun tile(point: Point): Tile {
        return tiles[point.y][point.x]
    }
}

data class Visit(val point: Point, val direction: Direction)

fun main() {
    // use -Xss8M to increase stack size
    puzzle(11048) { lines ->
        val maze = Maze(lines.map { it.map { c -> Tile.of(c) } })
        val minScore = mutableMapOf<Visit, Int?>()
        maze.print()

        val start = maze.tiles.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, tile ->
                if (tile == Tile.START) Point(
                    x,
                    y
                ) else null
            }
        }.flatten().first()

        moveReindeer(maze, start, Direction.RIGHT, minScore)!!
    }
}

fun moveReindeer(maze: Maze, start: Point, direction: Direction, minScore: MutableMap<Visit, Int?>): Int? {
    if (maze.tile(start) == Tile.END) {
        return 0
    }

    val visit = Visit(start, direction)
    if (minScore.containsKey(visit)) {
        return minScore[visit]
    }

    minScore[visit] = null // don't visit again

    val next = start.move(direction)
    val ahead =
        if (maze.tile(next) == Tile.WALL) {
            null
        } else {
            moveReindeer(maze, next, direction, minScore)?.let { 1 + it }
        }

    val left = moveReindeer(maze, start, direction.turnLeft(), minScore)?.let { 1000 + it }
    val right = moveReindeer(maze, start, direction.turnRight(), minScore)?.let { 1000 + it }

    val score = listOfNotNull(left, right, ahead).minOrNull()
    minScore[visit] = score
    return score
}
