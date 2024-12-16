package year2023.day2023_13

import Point
import puzzle

enum class Content(val symbol: Char) {
    EMPTY('.'),
    MIRROR('#');

    companion object {
        fun of(symbol: Char): Content = entries.single { it.symbol == symbol }
    }
}

data class Tile(val content: Content, val point: Point)

fun main() {
    puzzle(405, keepEmptyRows = true) { lines ->
        val valleys = lineBlocks(lines).map { block ->
            block.mapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    Tile(Content.of(c), Point(x, y))
                }
            }
        }
        valleys.sumOf { valley ->
            println("next valley")
            valley.forEach { row ->
                println(row.joinToString("") { it.content.symbol.toString() })
            }

            val value = (1 until valley.size)
                .filter {
                    val mirror = isVerticalMirror(valley, it)
                    if (mirror) {
                        println("Vertical mirror at $it")
                    }
                    mirror
                }.sumOf { it * 100 } +
                    (1 until valley[0].size)
                        .filter {
                            val mirror = isHorizontalMirror(valley, it)
                            if (mirror) {
                                println("Horizontal mirror at $it")
                            }
                            mirror
                        }.sum()
            if (value == 0) {
                throw Exception("No mirror found")
            }
            value
        }
    }
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

private fun lineBlocks(lines: List<String>): List<List<String>> {
    val result = mutableListOf<List<String>>()
    var remaining = lines
    while (true) {
        val i = remaining.indexOf("")
        if (i == -1) {
            result.add(remaining)
            return result
        }
        result.add(remaining.subList(0, i))
        remaining = remaining.subList(i + 1, remaining.size)
    }
}
