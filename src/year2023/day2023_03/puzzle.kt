package year2023.day2023_03

import puzzle

data class Number(val value: Int, val y: Int, val range: IntRange)

fun main() {

    puzzle(4361) { lines ->
        fun isSymbol(x: Int, y: Int): Boolean = x in lines[y].indices && run {
            val c = lines[y][x]
            !c.isDigit() && c != '.'
        }

        fun anySymbol(y: Int, from: Int, to: Int): Boolean =
            y in lines.indices
                    && (from until to + 1).any { x -> isSymbol(x, y)
        }

        fun hasAdjacentSymbol(number: Number): Boolean {
            val last = number.range.last
            val first = number.range.first
            return (isSymbol(first - 1, number.y)
                    || isSymbol(last + 1, number.y)
                    || anySymbol(number.y - 1, first - 1, last + 1)
                    || anySymbol(number.y + 1, first - 1, last + 1))
        }

        val isNum = "(\\d+)".toRegex()
        val list = lines.indices.flatMap { y ->
            isNum.findAll(lines[y]).map {
                val range = it.range
                Number(it.value.toInt(), y, range)
            }
        }
        val filter = list.filter { hasAdjacentSymbol(it) }
        println(filter.map { it.value }.joinToString(" "))
        filter.sumOf { it.value }
    }
}

