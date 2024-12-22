package year2015.day2015_06

import puzzle

val regex = Regex("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)")

fun main() {
    puzzle(null) { lines ->
        val lights = Array(1000) { BooleanArray(1000) }
        lines.forEach { instruction ->
            println(instruction)
            val match = regex.find(instruction) ?: throw IllegalArgumentException("Invalid instruction: $instruction")
            val (action, x1, y1, x2, y2) = match.destructured
            when(action) {
                "turn on" -> {
                    for (x in x1.toInt()..x2.toInt()) {
                        for (y in y1.toInt()..y2.toInt()) {
                            lights[x][y] = true
                        }
                    }
                }
                "turn off" -> {
                    for (x in x1.toInt()..x2.toInt()) {
                        for (y in y1.toInt()..y2.toInt()) {
                            lights[x][y] = false
                        }
                    }
                }
                "toggle" -> {
                    for (x in x1.toInt()..x2.toInt()) {
                        for (y in y1.toInt()..y2.toInt()) {
                            lights[x][y] = !lights[x][y]
                        }
                    }
                }
            }
        }
        lights.sumOf { it.count { it } }
    }
}
