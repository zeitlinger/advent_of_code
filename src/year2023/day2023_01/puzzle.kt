package year2023.day2023_01

import puzzle

fun main() {
    puzzle(281) { lines ->
         lines.sumOf { line ->
             val digits = line.indices.mapNotNull { start ->
                 digit(line.substring(start))
             }
             digits.first() * 10 + digits.last()
         }
    }
}

enum class Digit {
    one,
    two,
    three,
    four,
    five,
    six,
    seven,
    eight,
    nine
}

private fun digit(s: String): Int? = when {
    s.first().isDigit() -> s.first().digitToInt()
    else -> {
        Digit.entries.firstOrNull {
            s.startsWith(it.toString())
        }?.let { it.ordinal + 1 }
    }
}
