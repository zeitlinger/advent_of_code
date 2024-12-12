package year2023.day2023_08

import puzzle

enum class Turn {
    L, R
}

data class Instruction(val from: String, val left: String, val right: String)

fun main() {
    puzzle(6) { lines ->
        val turns = lines[0].toCharArray().map { Turn.valueOf(it.toString()) }
        val instructions = lines.drop(1).map {
            val s = it.split(" = ")
            val from = s[0].trim()
            val lr = s[1].substringAfter("(").substringBefore(")").split(", ")
            Instruction(from, lr[0], lr[1])
        }.associateBy { it.from }
        var steps = 0
        var turnIndex = 0
        var locations = instructions.keys.filter { it.endsWith("A") }
        while (true) {
//            println(locations)
            if (locations.all { it.endsWith("Z") }) {
                return@puzzle steps
            }
            locations = locations.map {
                val instruction = instructions[it]!!
                val turn = turns[turnIndex]
                if (turn == Turn.L) {
                    instruction.left
                } else {
                    instruction.right
                }
            }

            steps++
            if (steps % 1000 == 0) {
                println(steps)
            }

            turnIndex++
            if (turnIndex == turns.size) {
                turnIndex = 0
            }
        }
        throw IllegalStateException("Should not reach here")
    }
}
