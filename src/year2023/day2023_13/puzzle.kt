package year2023.day2023_13

import Point
import puzzle
import stringPuzzle

enum class Content(val symbol: Char) {
    EMPTY('.'),
    MIRROR('#');

    companion object {
        fun of(symbol: Char): Content = entries.single { it.symbol == symbol }
    }
}

data class Tile(var content: Content, val point: Point) {
    fun flip() {
        content = when (content) {
            Content.EMPTY -> Content.MIRROR
            Content.MIRROR -> Content.EMPTY
        }
    }
}

fun main() {
    stringPuzzle("400") { input ->
        val valleys = input.lineBlocks().map { block ->
            block.mapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    Tile(Content.of(c), Point(x, y))
                }
            }
        }
        valleys.sumOf { valley ->
            println("next valley")
            printValley(valley)
            val old = findReflection(valley, -1)

            valleyValue(valley, old)
        }.toString()
    }
}

private fun printValley(valley: List<List<Tile>>) {
    valley.forEach { row ->
        println(row.joinToString("") { it.content.symbol.toString() })
    }
}

private fun valleyValue(valley: List<List<Tile>>, old: Int): Int {
    valley.forEach { row ->
        row.forEach { tile ->
            tile.flip()
            val reflection = findReflection(valley, old)
            if (reflection > 0) {
                println("Reflection at ${tile.point} old $old new $reflection")
                printValley(valley)
                return reflection
            }
            tile.flip()
        }
    }
    throw IllegalStateException("No reflection found")
}

private fun findReflection(valley: List<List<Tile>>, old: Int): Int {
    val value = (1 until valley.size)
        .filter {
            val mirror = isVerticalMirror(valley, it)
            if (mirror) {
                println("Vertical mirror at $it")
            }
            mirror
        }.map { it * 100 } +
            (1 until valley[0].size)
                .filter {
                    val mirror = isHorizontalMirror(valley, it)
                    if (mirror) {
                        println("Horizontal mirror at $it")
                    }
                    mirror
                }
    return value.singleOrNull { it != old } ?: 0
}

fun isHorizontalMirror(valley: List<List<Tile>>, left: Int): Boolean {
    return valley.all { row ->
        row.all { tile ->
            val x = tile.point.x
            val mirror = if (x < left) {
                val dx = left - x
                left - 1 + dx
            } else {
                val dx = x - left
                left - 1 - dx
            }
            if (mirror < 0 || mirror >= row.size) {
                true
            } else {
                row[mirror].content == tile.content
            }
        }
    }
}

fun isVerticalMirror(valley: List<List<Tile>>, above: Int): Boolean {
    return valley.all { row ->
        val y = row[0].point.y
        val mirror = if (y < above) {
            val dy = above - y
            above - 1 + dy
        } else {
            val dy = y - above
            above - 1 - dy
        }
        if (mirror < 0 || mirror >= valley.size) {
            true
        } else {
            row.all { tile ->
                tile.content == valley[mirror][tile.point.x].content
            }
        }
    }
}
