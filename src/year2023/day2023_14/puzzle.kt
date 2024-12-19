package year2023.day2023_14

import Direction
import puzzle

enum class Content(val symbol: Char) {
    EMPTY('.'),
    CUBE('#'),
    ROUND('O'),
}

data class Tile(val content: Content)

data class Field(val tiles: MutableList<MutableList<Tile>>) {
    fun copy(): Field {
        return Field(tiles.map { it.toMutableList() }.toMutableList())
    }
}

fun main() {
    val directions = listOf(Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT)
    puzzle(64) { lines ->
        val field = Field(lines.map { row ->
            row.map { c ->
                Tile(Content.entries.first { it.symbol == c })
            }.toMutableList()
        }.toMutableList())
        printField(field)
        var cycle = 0
        val history = mutableListOf<Field>()
        while (true) {
            val needed = 1000000000
            if (cycle == needed) {
                break
            }
            if (history.contains(field)) {
                val index = history.indexOf(field)
                val cycleLength = cycle - index
                val remaining = needed - cycle
                val offset = remaining % cycleLength
                return@puzzle totalLoad(history[index + offset])
            }

            history.add(field.copy())
            directions.forEach { direction ->
                tiltAll(field, direction)
                println("Moved $direction")
                printField(field)
            }
            cycle++
        }
        totalLoad(field)
    }
}

fun printField(field: Field) {
    field.tiles.forEach { row ->
        println(row.joinToString("") { it.content.symbol.toString() })
    }
    println()
}

fun tiltAll(field: Field, direction: Direction) {
    val tiles = if (direction == Direction.DOWN) field.tiles.indices.reversed() else field.tiles.indices
    tiles.forEach { y ->
        val row = field.tiles[y]
        val list = if (direction == Direction.RIGHT) row.indices.reversed() else row.indices
        list.forEach { x ->
            val tile = field.tiles[y][x]
            if (tile.content == Content.ROUND) {
                tiltRound(field, y, x, direction)
            }
        }
    }
}

private fun tiltRound(
    field: Field,
    y: Int,
    x: Int,
    direction: Direction,
) {
    val lastRow = field.tiles.getOrNull(y + direction.y)
    if (lastRow != null) {
        val nextTile = lastRow.getOrNull(x + direction.x)
        if (nextTile != null && nextTile.content == Content.EMPTY) {
            lastRow[x + direction.x] = Tile(Content.ROUND)
            field.tiles[y][x] = Tile(Content.EMPTY)
            tiltRound(field, y + direction.y, x + direction.x, direction)
        }
    }
}

fun totalLoad(field: Field): Int {
    return field.tiles.mapIndexed { index, tiles ->
        tiles.count { it.content == Content.ROUND } * (field.tiles.size - index)
    }.sum()
}
