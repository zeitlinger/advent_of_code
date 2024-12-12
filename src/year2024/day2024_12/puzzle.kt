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
        val input = lines.map { it.toCharArray().toMutableList() }
        val maxX = input.maxOf { it.size }
        var regions = 0
        val regionMap: MutableList<MutableList<Region?>> = MutableList(input.size) { MutableList(maxX) { null } }

        fun addRegion(x: Int, y: Int, region: Region) {
            region.gardens += Point(x, y)
            regionMap[y][x] = region
            input[y][x] = '.'

            Direction.entries.forEach {
                val nx = x + it.dx
                val ny = y + it.dy
                if (input.getOrNull(ny)?.getOrNull(nx) == region.letter) {
                    addRegion(nx, ny, region)
                }
            }
        }

        fun price(region: Region): Long {
            val fenceLen = region.gardens.sumOf { point ->
                Direction.entries.count { dir ->
                    val nx = point.x + dir.dx
                    val ny = point.y + dir.dy
                    regionMap.getOrNull(ny)?.getOrNull(nx) != region
                }
            }
            return fenceLen.toLong() * region.gardens.size
        }

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, _ ->
                val letter = input[y][x]
                if (letter != '.') {
                    addRegion(x, y, Region(regions++, letter, mutableListOf()))
                }
            }
        }

        regionMap.flatten().toSet().sumOf { price(it!!) }
    }
}

