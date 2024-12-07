package year2023.day2023_02

import puzzle

fun main() {
    puzzle(8) { lines ->
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        lines.sumOf { line ->
            val (a, b) = line.split(":")
            val game = a.split(" ").let { it[1].toInt() }
            val sets = b.split(";")
            val possible = sets.all {
                it.split(",").all {
                    val (count, color) = it.trim().split(" ")
                    when (color) {
                        "blue" -> count.toInt() <= 14
                        "red" -> count.toInt() <= 12
                        "green" -> count.toInt() <= 13
                        else -> false
                    }
                }
            }
            if (possible) game else 0
        }
    }
}
