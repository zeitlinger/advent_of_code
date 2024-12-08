package year2023.day2023_03

import puzzle

data class Number(val value: Int, val y: Int, val range: IntRange)

fun main() {

    puzzle(2286) { lines ->
        fun isSymbol(x: Int, y: Int): Boolean = x in lines[y].indices && run {
            val c = lines[y][x]
            !c.isDigit() && c != '.'
        }

        fun anySymbol(y: Int, from: Int, to: Int): Boolean =
            y in lines.indices
                    && (from until to + 1).any { x -> isSymbol(x, y)
        }

        fun hasAdjacentSymbol(number: Number): Boolean = isSymbol(number.range.first - 1, number.y)
                || isSymbol(number.range.last + 1, number.y)
                || anySymbol(number.y - 1, number.range.first - 1, number.range.last + 1)
                || anySymbol(number.y + 1, number.range.first - 1, number.range.last + 1)

        val isNum = "(\\d+)".toRegex()
        val list = lines.indices.flatMap { y ->
            isNum.findAll(lines[y]).map {
                val range = it.range
                Number(it.value.toInt(), y, range)
            }
        }
        val filter = list.filter { hasAdjacentSymbol(it) }
        filter.sumOf { it.value }
    }
}

