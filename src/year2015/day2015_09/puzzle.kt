package year2015.day2015_09

import permutations
import puzzle

data class Connection(val route: List<String>, val distance: Int)

fun main() {
    puzzle(982) { lines ->
        lines.map {
            val (from, to, distance) = it.split(" to ", " = ")
            Connection(listOf(from, to), distance.toInt())
        }.let { connections ->
            val cities = connections.flatMap { it.route }.toSet()
            cities.permutations().map { path ->
                path.windowed(2).sumOf { (from, to) ->
                    connections.first { from in it.route && to in it.route }.distance
                }
            }.let { it.maxOrNull()!! }
        }
    }
}
