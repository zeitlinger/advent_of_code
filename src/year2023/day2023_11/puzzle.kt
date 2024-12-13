package year2023.day2023_11

import puzzle
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int) {}

data class Galaxy(val id: Int, val location: Point)

fun main() {
    puzzle(374) { lines ->
        val galaxyBeforeExpansion = lines.map { it.toMutableList() }.toMutableList()
        val galaxy = expandGalaxy(galaxyBeforeExpansion)
        // permutations of pairs
        val pairs = galaxy.map { g1 -> galaxy.map { g2 -> g1 to g2 } }
            .flatten()
            .filter { (g1, g2) -> g1.id < g2.id }
        // calculate the distance between each pair
        val distances = pairs.map { (g1, g2) ->
            val dx = g1.location.x - g2.location.x
            val dy = g1.location.y - g2.location.y
//            val d = sqrt((dx * dx + dy * dy).toDouble()).roundToInt()
            val d = dx.absoluteValue + dy.absoluteValue
            println("Distance between ${g1.id} and ${g2.id} is $d")
            d
        }.sum()
        distances
    }
}

fun expandGalaxy(galaxy: MutableList<MutableList<Char>>): List<Galaxy> {
    galaxy.indices.reversed().forEach { y ->
        if (!galaxy[y].contains('#')) {
            galaxy.add(y, galaxy[y].toMutableList())
        }
    }
    galaxy[0].indices.reversed().forEach { x ->
        if (galaxy.all { it[x] == '.' }) {
            galaxy.forEachIndexed { y, it ->
                it.add(x, '.')
            }
        }
    }

    println(galaxy.map { it.joinToString("") }.joinToString("\n"))

    var id = 0
    return galaxy.mapIndexedNotNull { y, row ->
        row.mapIndexedNotNull { x, c ->
            if (c == '#') {
                id++
                Galaxy(id, Point(x, y))
            } else {
                null
            }
        }
    }.flatten()
}
