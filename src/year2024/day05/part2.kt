package year2024.day05

import puzzle

fun main() {
    puzzle(123) { lines ->
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

        fun fixOrder(updates: MutableList<Int>) {
            return rules.forEach {
                val (a, b) = it
                val aIndex = updates.indexOf(a)
                val bIndex = updates.indexOf(b)

                if (aIndex >= 0 && bIndex >= 0 && aIndex > bIndex) {
                    val temp = updates[aIndex]
                    updates[aIndex] = updates[bIndex]
                    updates[bIndex] = temp
                    // call again to fix the rest
                    fixOrder(updates)
                }
            }
        }

        val updates = lines
            .filter { "|" !in it }
            .map { it.split(",").map { it.toInt() } }
            .filter { !isInRightOrder(it) }
            .sumOf {
                println("not in order: $it")
                val mut = it.toMutableList()
                fixOrder(mut)
                println("fixed order: $mut")
                mut[it.size / 2]
            }

        updates
    }
}
