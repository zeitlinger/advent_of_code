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
        var location = "AAA"
        while (true) {
            if (location == "ZZZ") {
                return@puzzle steps
            }
            val instruction = instructions[location]!!
            val turn = turns[turnIndex]
            location = if (turn == Turn.L) {
                instruction.left
            } else {
                instruction.right
            }
            steps++

            turnIndex++
            if (turnIndex == turns.size) {
                turnIndex = 0
            }
        }
        throw IllegalStateException("Should not reach here")
    }
}
