package year2023.day2023_14

import Point
import puzzle

enum class Content(val symbol: Char) {
    EMPTY('.'),
    CUBE('#'),
    ROUND('O'),
}

data class Tile(val content: Content, val point: Point)

fun main() {
    puzzle(136) { lines ->
        val field = lines.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                Tile(Content.entries.first { it.symbol == c }, Point(x, y))
            }.toMutableList()
        }.toMutableList()
        printField(field)
        tiltUp(field)
        printField(field)
        totalLoad(field)
    }
}

fun printField(field: MutableList<MutableList<Tile>>) {
    field.forEach { row ->
        println(row.joinToString("") { it.content.symbol.toString() })
    }
    println()
}

fun tiltUp(field: MutableList<MutableList<Tile>>) {
  field.forEachIndexed { y, row ->
    row.forEachIndexed { x, tile ->
      if (tile.content == Content.ROUND) {
          tiltRound(field, y, x, tile)
      }
    }
  }
}

private fun tiltRound(
    field: MutableList<MutableList<Tile>>,
    y: Int,
    x: Int,
    tile: Tile
) {
    val lastRow = field.getOrNull(y - 1)
    if (lastRow != null) {
        val nextTile = lastRow.getOrNull(x)
        if (nextTile != null && nextTile.content == Content.EMPTY) {
            lastRow[x] = Tile(Content.ROUND, nextTile.point)
            field[y][x] = Tile(Content.EMPTY, tile.point)
            tiltRound(field, y - 1, x, nextTile)
        }
    }
}

fun totalLoad(field: MutableList<MutableList<Tile>>): Int {
    return field.mapIndexed { index, tiles ->
        tiles.count { it.content == Content.ROUND } * (field.size - index)
    }.sum()
}
