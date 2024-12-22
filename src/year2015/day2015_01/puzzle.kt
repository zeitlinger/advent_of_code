package year2015.day2015_01

import puzzle

fun main() {
    puzzle(null) { lines ->
        var floor = 0
        lines[0].forEach {
            when (it) {
                '(' -> floor++
                ')' -> floor--
            }
        }
        floor
    }
}
