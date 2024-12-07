package year2023.day2023_03

import puzzle

fun main() {
    puzzle(2286) { lines ->
        val isNum = "(\\d+)".toRegex()
        val list = lines.indices.flatMap { y ->
            isNum.findAll(lines[y]).map {
                val range = it.range
                y to range
            }
        }
        list.filter { hasAdjacentSymbol(it, lines) }.sum
        0
    }
}
