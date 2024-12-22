package year2015.day2015_05

import puzzle

fun main() {
    puzzle(1) { lines ->
        lines.count { nice(it) }
    }
}

val vowels = "aeiou".toSet()
val forbidden = setOf("ab", "cd", "pq", "xy")

fun nice(s: String): Boolean {
    val vowels = s.count { it in vowels }
    val double = s.zipWithNext().any { it.first == it.second }
    val forbidden = forbidden.any { it in s }
    return vowels >= 3 && double && !forbidden
}
