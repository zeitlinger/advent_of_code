package year2023.day2023_02

import puzzle

data class Game(val blue: Int, val red: Int, val green: Int) {
    fun max(o: Game): Game {
        return Game(
            blue = blue.coerceAtLeast(o.blue),
            red = red.coerceAtLeast(o.red),
            green = green.coerceAtLeast(o.green)
        )
    }
}

fun main() {
    puzzle(2286) { lines ->
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        lines.sumOf { line ->
            val (a, b) = line.split(":")
            val id = a.split(" ").let { it[1].toInt() }
            val min = b.split(";").map {
                val map = it.split(",").map {
                    val (count, color) = it.trim().split(" ")
                    color to count.toInt()
                }.toMap()
                Game(
                    blue = map.getOrDefault("blue", 0),
                    red = map.getOrDefault("red", 0),
                    green = map.getOrDefault("green", 0)
                )
            }.fold(Game(0, 0, 0)) { acc, game -> acc.max(game) }
            val power = min.blue * min.red * min.green
            power
        }
    }
}
