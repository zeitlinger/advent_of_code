package year2023.day2023_16

import Direction
import Point
import puzzle

//Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid containing empty space (.), mirrors (/ and \), and splitters (| and -).

enum class Content(val symbol: Char) {
    EMPTY('.'),
    MIRROR_LEFT('/'),
    MIRROR_RIGHT('\\'),
    SPLITTER_VERTICAL('|'),
    SPLITTER_HORIZONTAL('-');

    companion object {
        fun of(c: Char): Content {
            return entries.single { it.symbol == c }
        }
    }

    fun output(direction: Direction): List<Direction> {
        return when (this) {
            EMPTY -> listOf(direction)
            MIRROR_LEFT -> when (direction) {
                Direction.UP -> listOf(Direction.RIGHT)
                Direction.RIGHT -> listOf(Direction.UP)
                Direction.DOWN -> listOf(Direction.LEFT)
                Direction.LEFT -> listOf(Direction.DOWN)
            }

            MIRROR_RIGHT -> when (direction) {
                Direction.UP -> listOf(Direction.LEFT)
                Direction.LEFT -> listOf(Direction.UP)
                Direction.DOWN -> listOf(Direction.RIGHT)
                Direction.RIGHT -> listOf(Direction.DOWN)
            }

            SPLITTER_HORIZONTAL -> when (direction) {
                Direction.UP -> listOf(Direction.LEFT, Direction.RIGHT)
                Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
                else -> listOf(direction)
            }

            SPLITTER_VERTICAL -> when (direction) {
                Direction.LEFT -> listOf(Direction.UP, Direction.DOWN)
                Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
                else -> listOf(direction)
            }
        }
    }
}

data class Tile(
    val content: Content,
    var energizedVertical: Boolean = false,
    var energizedHorizontal: Boolean = false
) {
    fun energize(direction: Direction): Boolean {
        when (direction) {
            Direction.UP, Direction.DOWN -> {
                if (energizedVertical) {
                    return false
                }
                energizedVertical = true
            }

            Direction.LEFT, Direction.RIGHT -> {
                if (energizedHorizontal) {
                    return false
                }
                energizedHorizontal = true
            }
        }
        return true
    }

    fun isEnergized(): Boolean {
        return energizedVertical || energizedHorizontal
    }
}

data class Field(val tiles: MutableList<MutableList<Tile>>) {
    fun energy(point: Point, direction: Direction) {
        val tile = tiles[point.y][point.x]
        if (!tile.energize(direction)) {
            return
        }
        val output = tile.content.output(direction)
        println("Energizing $point $direction ${tile.content} -> $output")
        print()
        output.forEach { outputDirection ->
            val nextPoint = point.move(outputDirection)
            if (nextPoint.x in tiles[0].indices && nextPoint.y in tiles.indices) {
                energy(nextPoint, outputDirection)
            }
        }
    }

    fun print() {
        tiles.forEach { row ->
            println(row.joinToString("") { if (it.isEnergized()) "#" else " " })
        }
    }
}


fun main() {
    puzzle(46) { lines ->
        val field = Field(lines.map { row ->
            row.map { c ->
                Tile(Content.of(c))
            }.toMutableList()
        }.toMutableList())
        field.energy(Point(0, 0), Direction.RIGHT)
        0
    }
}
