package year2024.day2024_12

import puzzle
import kotlin.math.pow

data class Point(val x: Int, val y: Int)

data class Fence(val point: Point, val direction: Direction)

data class Region(
    val id: Int,
    val letter: Char,
    val gardens: MutableList<Point>,
    val fences: MutableList<Fence> = mutableListOf()
)

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}

fun main() {
    puzzle(1206) { lines ->
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

        fun neighbor(
            point: Point,
            dir: Direction,
            regionMap: MutableList<MutableList<Region?>>
        ): Region? {
            val nx = point.x + dir.dx
            val ny = point.y + dir.dy
            return regionMap.getOrNull(ny)?.getOrNull(nx)
        }

        fun hasAdjacentFenceInPriorGarden(garden: Point, dir: Direction, region: Region): Boolean {
            val myIndex = region.gardens.indexOf(garden)
            return Direction.entries.firstOrNull {
                val nx = garden.x + it.dx
                val ny = garden.y + it.dy
                val neighbor = Point(nx, ny)
                val n = neighbor(neighbor, dir, regionMap)
                val fence = n == region
                val nIndex = region.gardens.indexOf(neighbor)
                fence && nIndex in 0 until myIndex
            } != null
        }

        fun price(region: Region): Long {
            val fenceLen = region.gardens.sumOf { garden ->
                Direction.entries.count { dir ->
                    val hasFence = neighbor(garden, dir, regionMap) != region &&
                            !hasAdjacentFenceInPriorGarden(garden, dir, region)
                    if (hasFence) {
                        region.fences += Fence(garden, dir)
                    }
                    hasFence
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

        val set = regionMap.flatten().toSet()
        val price = set.sumOf { price(it!!) }
        set.forEach {
            printRegion(it!!)
        }
        price
    }
}

fun printRegion(region: Region) {
    println("Region ${region.id} with letter ${region.letter} has ${region.gardens.size} gardens and ${region.fences.size} fences")
    val minX = region.fences.minOf { it.point.x }
    val minY = region.fences.minOf { it.point.y }
    val maxX = region.fences.maxOf { it.point.x }
    val maxY = region.fences.maxOf { it.point.y }
    val fenceMap = region.fences.groupBy { it.point }
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val point = Point(x, y)
            val v = fenceMap[point]?.let {
                it.map { 2.0.pow(it.direction.ordinal) }.sum()
            }
//            val fence = region.fences.count { it.point == point }
            val fence = v?.toInt() ?: 0
            val indexOf = region.gardens.indexOf(point)
            val c = when {
                fence > 0 -> fence.toString()
                indexOf >= 0 -> "G"
                else -> " "
            }
            print(if (indexOf < 0) "   " else indexOf.toString().padStart(3, ' '))
            print("=")
            print(c)
        }
        println()
    }
}

