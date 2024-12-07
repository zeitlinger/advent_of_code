package year2024.day03

import puzzle

fun main() {
    puzzle(48) { lines ->
        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|do(n't)?\\(\\)".toRegex()
        var enabled = true
        lines.map {
            println(it)
            regex.findAll(it).map {
                val s = it.groupValues[0]
                println(s)
                when {
                    s.startsWith("mul") -> {
                        val (a, b) = it.destructured
                        (a.toInt() * b.toInt()).let {
                            if (enabled) {
                                println(it)
                                it
                            } else {
                                0
                            }
                        }
                    }

                    s.startsWith("don't") -> {
                        println("don't")
                        enabled = false
                        0
                    }

                    s.startsWith("do") -> {
                        println("do")
                        enabled = true
                        0
                    }

                    else -> 0
                }
            }.sum()
        }.sum()
    }
}

