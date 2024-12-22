package year2015.day2015_01

import puzzle

fun main() {
    puzzle(null) { lines ->
        getFloor(lines)
    }
}

private fun getFloor(lines: List<String>): Int {
    var floor = 0
    lines[0].forEachIndexed { index, c ->
        when (c) {
            '(' -> floor++
            ')' -> floor--
        }
        if (floor == -1) {
            println("Basement: ${index + 1}")
            return index + 1
        }
    }
    return floor
}
