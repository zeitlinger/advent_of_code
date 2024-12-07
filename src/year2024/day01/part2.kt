package year2024.day01

import puzzle

fun main() {
    puzzle(31) {
        val list = it
            .map { it.split(" ").filter { it.isNotBlank() } }
        val first = list.map { it[0].toInt() }.sorted()
        val second = list.map { it[1].toInt() }.sorted()
        first.map { f -> f * second.filter { it == f }.count() }.sum()
    }
}
