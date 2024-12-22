package year2015.day2015_12

import puzzle

val regex = Regex("""(-?\d+)""")

fun main() {
    puzzle(null) { lines ->
        val numbers = regex.findAll(lines[0]).map { it.value.toInt() }.toList()
        numbers.sum()
    }
}
