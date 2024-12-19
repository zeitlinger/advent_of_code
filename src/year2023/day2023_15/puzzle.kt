package year2023.day2023_15

import puzzle

fun main() {
    puzzle(1320) { lines ->
        lines[0].split(",").sumOf {
            hash(it)
        }
    }
}

fun hash(s: String): Int {
    var current = 0
    for (c in s) {
        val code = c.code
        current += code
        current *= 17
        current %= 256
    }
    return current
}
