package year2024.day2024_08

import puzzle

data class Point(val x: Int, val y: Int)

data class Antenna(val loc: Point, val frequency: Char)

fun main() {
    puzzle(14) { lines ->
        val maxX = lines.maxOf { it.length }
        val maxY = lines.size
        val uniqueLocations = mutableSetOf<Point>()
        lines.flatMapIndexed { y: Int, line: String ->
            line.mapIndexed { x: Int, c: Char ->
                if (c != '.') Antenna(Point(x, y), c) else null
            }
        }
            .filterNotNull()
            .groupBy { it.frequency }.values
            .map { antennas ->
                antennas.map { antenna ->
                    antennas.filter { it != antenna }
                        .forEach { other ->
                            val dx = antenna.loc.x - other.loc.x
                            val dy = antenna.loc.y - other.loc.y
                            val targetX = other.loc.x + 2 * dx
                            val targetY = other.loc.y + 2 * dy
                            if (targetX in 0 until maxX && targetY in 0 until maxY) {
                                uniqueLocations.add(Point(targetX, targetY))
                            }
                        }
                }
            }
        uniqueLocations.size
    }
}

