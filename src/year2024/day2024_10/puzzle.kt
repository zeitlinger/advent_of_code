package year2024.day2024_10

import puzzle

data class Point(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}

typealias Path = List<Point>

fun main() {
    puzzle(81) { lines ->
        val map = lines.map { it.map { it.digitToInt() } }
        val height = map.size
        val width = map[0].size

        fun score(point: Point, wantAltitude: Int, path: Path): List<Path> {
            val x = point.x
            val y = point.y
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return emptyList()
            }
            val rightAlt = map[y][x] == wantAltitude
            if (!rightAlt) {
                return emptyList()
            }
            val nextPath = path + point
            if (wantAltitude == 9) {
                println(nextPath)
                return listOf(nextPath)
            }
            return Direction.entries.map { direction ->
                score(Point(x + direction.dx, y + direction.dy), wantAltitude + 1, nextPath)
            }.fold(emptyList()) { acc, list -> acc + list }
        }

        map.indices.sumOf { y ->
            map[y].indices.sumOf { x ->
                val score = score(Point(x, y), 0, emptyList())
                if (score.isNotEmpty()) {
                    println("found at $x, $y with score $score")
                }
                score.filter { it.isNotEmpty() }.map { it.last() }.toSet().size
            }
        }
    }
}
