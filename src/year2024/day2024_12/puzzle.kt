package year2024.day2024_12

import puzzle

data class Point(val x: Int, val y: Int)

data class Region(val id: Int, val letter: Char, val gardens: MutableList<Point>)

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}

fun main() {
    puzzle(1930) { lines ->
        val maxX = lines.maxOf { it.length }
        var regions = 0
        val regionMap: MutableList<MutableList<Region?>> = MutableList(lines.size) { MutableList(maxX) { null } }

        fun addRegion(letter: Char, x: Int, y: Int) {
            val old = Direction.entries.firstOrNull {
                val nx = x + it.dx
                val ny = y + it.dy
                regionMap.getOrNull(ny)?.getOrNull(nx)?.letter == letter
            }?.let { regionMap[y + it.dy][x + it.dx] }

            if (old != null) {
                regionMap[y][x] = old
                old.gardens += Point(x, y)
            } else {
                regionMap[y][x] = Region(regions++, letter, mutableListOf(Point(x, y)))
            }
        }

        fun price(region: Region): Long {
            println(region)
            return 0
        }

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, letter ->
                if (letter != '.') {
                    addRegion(letter, x, y)
                }
            }
        }

        regionMap.flatten().toSet().sumOf { price(it!!) }
    }
}

