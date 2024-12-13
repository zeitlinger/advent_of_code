package year2023.day2023_11

import puzzle
import kotlin.math.absoluteValue

data class Point(var x: Long, var y: Long) {}

data class Galaxy(val id: Int, val location: Point)

fun main() {
    puzzle(null) { lines ->
        val galaxyBeforeExpansion = lines.map { it.toList() }
        val galaxy = expandGalaxy(galaxyBeforeExpansion)
        // permutations of pairs
        val pairs = galaxy.map { g1 -> galaxy.map { g2 -> g1 to g2 } }
            .flatten()
            .filter { (g1, g2) -> g1.id < g2.id }
        // calculate the distance between each pair
        val distances = pairs.map { (g1, g2) ->
            val dx = g1.location.x - g2.location.x
            val dy = g1.location.y - g2.location.y
            val d = dx.absoluteValue + dy.absoluteValue
//            println("Distance between ${g1.id} and ${g2.id} is $d")
            d
        }.sum()
        distances
    }
}

fun expandGalaxy(galaxy: List<List<Char>>): List<Galaxy> {
//    println(galaxy.map { it.joinToString("") }.joinToString("\n"))

    var id = 0
    val galaxies = galaxy.mapIndexed { y, row ->
        row.mapIndexedNotNull { x, c ->
            if (c == '#') {
                id++
                Galaxy(id, Point(x.toLong(), y.toLong()))
            } else {
                null
            }
        }
    }.flatten()

    val add = 1_000_000 - 1

    galaxy.indices.reversed().forEach { y ->
        if (galaxies.all { it.location.y != y.toLong() }) {
            galaxies.filter { it.location.y > y }.forEach { it.location.y += add }
        }
    }
    galaxy[0].indices.reversed().forEach { x ->
        if (galaxies.all { it.location.x != x.toLong() }) {
            galaxies.filter { it.location.x > x }.forEach { it.location.x += add }
        }
    }

//    print(galaxies)

    return galaxies
}

private fun print(galaxies: List<Galaxy>) {
    for (y in 0..galaxies.maxOf { it.location.y }) {
        val list = galaxies.filter { it.location.y == y }
        if (list.isEmpty()) {
            println()
            continue
        }
        for (x in 0..list.maxOf { it.location.x }) {
            val g = galaxies.find { it.location.x == x && it.location.y == y }
            print(g?.id ?: '.')
        }
        println()
    }
}
