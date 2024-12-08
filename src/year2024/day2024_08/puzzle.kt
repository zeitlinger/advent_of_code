package year2024.day2024_08

import puzzle

data class Point(val x: Int, val y: Int)

data class Antenna(val loc: Point, val frequency: Char)

fun main() {
    puzzle(34) { lines ->
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
                            var targetX = other.loc.x + dx
                            var targetY = other.loc.y + dy
                            while (targetX in 0 until maxX && targetY in 0 until maxY) {
                                uniqueLocations.add(Point(targetX, targetY))
                                targetX += dx
                                targetY += dy
                            }
                        }
                }
            }
        val net = lines.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (Point(x, y) in uniqueLocations) '#' else c
            }.joinToString("")
        }.joinToString("\n")
        println(net)
        uniqueLocations.size
    }
}

