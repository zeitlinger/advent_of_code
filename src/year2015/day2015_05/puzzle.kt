package year2015.day2015_05

import puzzle

fun main() {
    puzzle(1) { lines ->
        lines.count { nice(it) }
    }
}

fun nice(s: String): Boolean {
    val double = 0.until(s.length - 2).any {
        val other = s.substring(it + 2).indexOf(s.substring(it, it + 2))
        other != -1
    }
    val palindrome = 1.until(s.length - 1).any {
        s[it - 1] == s[it + 1]
    }

    return double && palindrome
}
