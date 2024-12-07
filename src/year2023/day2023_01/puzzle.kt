package year2023.day2023_01

import puzzle

fun main() {
    puzzle(142) { lines ->
         lines.sumOf {
             (it.first { it.isDigit() }.toString() + it.last { it.isDigit() }).toInt()
         }
    }
}
