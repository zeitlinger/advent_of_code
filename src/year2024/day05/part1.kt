package year2024.day05

import puzzle

fun main() {
    puzzle(143) { lines ->
        val rules = lines
            .filter { "|" in it }
            .map { it.split("|") }
            .map { it[0].toInt() to it[1].toInt() }

        fun isInRightOrder(updates: List<Int>): Boolean {
            return rules.all {
                val (a, b) = it
                val aIndex = updates.indexOf(a)
                val bIndex = updates.indexOf(b)

                aIndex < 0 || bIndex < 0 || aIndex < bIndex
            }
        }

        val updates = lines
            .filter { "|" !in it }
            .map { it.split(",").map { it.toInt() } }
            .filter { isInRightOrder(it) }
            .sumOf {
                println("in order: $it")
                it[it.size / 2]
            }

        updates
    }
}
