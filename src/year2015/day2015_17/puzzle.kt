package year2015.day2015_17

import puzzle
import stringPuzzle

fun main() {
    stringPuzzle("3") { input ->
        val total = if (input.test) 25 else 150
        val containers = input.lines.map { it.toInt() }
        val list = combinations(total, containers, emptyList())
        val l = list.minOf { it.size }
        list.count { it.size == l }.toString()
    }
}

fun combinations(total: Int, containers: List<Int>, taken: List<Int>): List<List<Int>> {
    if (total == 0) {
        return listOf(taken)
    }
    if (total <= 0) {
        return emptyList()
    }
    val first = containers.first()
    return if (containers.size == 1) {
        if (total == first) {
            listOf(taken + first)
        } else {
            emptyList()
        }
    } else {
        val rest = containers.drop(1)
        val withFirst = combinations(total - first, rest, taken + first)
        val withoutFirst = combinations(total, rest, taken)
        withFirst + withoutFirst
    }
}
