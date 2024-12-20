package year2023.day2023_16

import Direction
import Point
import Visit
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
    var energized: MutableSet<Direction> = mutableSetOf()
) {
    fun energize(direction: Direction): Boolean {
        return energized.add(direction)
    }

    fun isEnergized(): Boolean {
        return energized.isNotEmpty()
    }
}

data class Field(val tiles: MutableList<MutableList<Tile>>) {
    fun energy(visit: Visit): List<Visit> {
        val point = visit.point
        val direction = visit.direction
        val tile = tiles[point.y][point.x]
        if (!tile.energize(direction)) {
            return emptyList()
        }
        val output = tile.content.output(direction)
//        println("Energizing $point $direction ${tile.content} -> $output")
//        print()
        return output.mapNotNull { outputDirection ->
            val nextPoint = point.move(outputDirection)
            if (nextPoint.x in tiles[0].indices && nextPoint.y in tiles.indices) {
                Visit(nextPoint, outputDirection)
            } else {
                null
            }
        }
    }

    fun copy(): Field {
        return Field(tiles.map { it.map { it.copy(energized = mutableSetOf()) }.toMutableList() }.toMutableList())
    }

    fun print() {
        tiles.forEach { row ->
            println(row.joinToString("") { if (it.isEnergized()) "#" else " " })
        }
    }
}


fun main() {
    puzzle(51) { lines ->
        val field = Field(lines.map { row ->
            row.map { c ->
                Tile(Content.of(c))
            }.toMutableList()
        }.toMutableList())
        val start = Visit(Point(0, 0), Direction.RIGHT)
        val row = field.tiles[0]
        val top = row.indices.map { Visit(Point(it, 0), Direction.DOWN) }
        val bottom = row.indices.map { Visit(Point(it, field.tiles.size - 1), Direction.UP) }
        val left = field.tiles.indices.map { Visit(Point(0, it), Direction.RIGHT) }
        val right = field.tiles.indices.map { Visit(Point(row.size - 1, it), Direction.LEFT) }
        val all = listOf(top, bottom, left, right).flatten()
        all.maxOf { energize(it, field.copy()) }
    }
}

private fun energize(start: Visit, field: Field): Int {
    val open = mutableSetOf(start)
    while (open.isNotEmpty()) {
        val visit = open.first()
        open.remove(visit)
        val nextVisits = field.energy(visit)
        open.addAll(nextVisits)
    }
    val sumOf = field.tiles.sumOf { row -> row.count { it.isEnergized() } }
    println("start $start result $sumOf")
    field.print()
    return sumOf
}
