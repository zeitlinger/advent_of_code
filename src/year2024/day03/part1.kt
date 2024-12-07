package day03

import puzzle

fun main() {
    puzzle(161) { lines ->
        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
        lines.map {
            println(it)
            regex.findAll(it).map {
                val (a, b) = it.destructured
                println(it.value)
                println(it.range)
                (a.toInt() * b.toInt()).also { println(it) }
            }.sum()
        }.sum()
    }
}
