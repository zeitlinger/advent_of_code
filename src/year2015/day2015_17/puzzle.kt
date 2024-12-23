package year2015.day2015_17

import puzzle
import stringPuzzle

fun main() {
    stringPuzzle("4") { input ->
        val total = if (input.test) 25 else 150
        val containers = input.lines.map { it.toInt() }
        combinations(total, containers, emptyList()).toString()
    }
}

fun combinations(total: Int, containers: List<Int>, taken: List<Int>): Int {
    if (total == 0) {
        return 1
    }
    if (total <= 0) {
        return 0
    }
    val first = containers.first()
    return if (containers.size == 1) {
        if (total == first) {
            1
        } else {
            0
        }
    } else {
        val rest = containers.drop(1)
        val withFirst = combinations(total - first, rest, taken + first)
        val withoutFirst = combinations(total, rest, taken)
        withFirst + withoutFirst
    }
}
