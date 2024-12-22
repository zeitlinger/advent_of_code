package year2015.day2015_06

import puzzle

val regex = Regex("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)")

fun main() {
    puzzle(null) { lines ->
        val lights = Array(1000) { Array(1000) { 0 } }
        lines.forEach { instruction ->
            println(instruction)
            val match = regex.find(instruction) ?: throw IllegalArgumentException("Invalid instruction: $instruction")
            val (action, x1, y1, x2, y2) = match.destructured
            when (action) {
                "turn on" -> {
                    for (x in x1.toInt()..x2.toInt()) {
                        for (y in y1.toInt()..y2.toInt()) {
                            lights[x][y] += 1
                        }
                    }
                }

                "turn off" -> {
                    for (x in x1.toInt()..x2.toInt()) {
                        for (y in y1.toInt()..y2.toInt()) {
                            if (lights[x][y] > 0) {
                                lights[x][y] -= 1
                            }
                        }
                    }
                }

                "toggle" -> {
                    for (x in x1.toInt()..x2.toInt()) {
                        for (y in y1.toInt()..y2.toInt()) {
                            lights[x][y] += 2
                        }
                    }
                }
            }
        }
        lights.sumOf { it.sumOf { it } }
    }
}
