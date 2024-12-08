package year2023.day2023_03

import puzzle

data class Point(val x: Int, val y: Int)

data class Number(val value: Int, val y: Int, val range: IntRange)

fun main() {
    puzzle(467835) { lines ->
        val factoryByGear = mutableMapOf<Point, MutableList<Int>>()

        fun isSymbol(x: Int, y: Int, number: Number): Boolean = x in lines[y].indices && run {
            val c = lines[y][x]
            if (c == '*') {
                val point = Point(x, y)
                factoryByGear.putIfAbsent(point, mutableListOf())
                factoryByGear[point]!!.add(number.value)
                return true
            }
            return false
        }

        fun anySymbol(y: Int, from: Int, to: Int, number: Number): Boolean =
            y in lines.indices
                    && (from until to + 1).any { x -> isSymbol(x, y, number)
        }

        fun hasAdjacentSymbol(number: Number): Boolean {
            val last = number.range.last
            val first = number.range.first
            return (isSymbol(first - 1, number.y, number)
                    || isSymbol(last + 1, number.y, number)
                    || anySymbol(number.y - 1, first - 1, last + 1, number)
                    || anySymbol(number.y + 1, first - 1, last + 1, number))
        }

        val isNum = "(\\d+)".toRegex()
        val list = lines.indices.flatMap { y ->
            isNum.findAll(lines[y]).map {
                val range = it.range
                Number(it.value.toInt(), y, range)
            }
        }
        list.filter { hasAdjacentSymbol(it) }
        println(factoryByGear)
        factoryByGear.values.filter { it.size == 2 }.map { it[0] *  it[1] }.sum()
    }
}

