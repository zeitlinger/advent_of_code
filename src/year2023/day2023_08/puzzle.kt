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

        val locations = instructions.keys.filter { it.endsWith("A") }
        val needed = locations.map {
            stepsNeeded(instructions, turns, it)
        }
        leastCommonMultiple(needed)
    }
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    return a * b / greatestCommonDivisor(a, b)
}

fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) a else greatestCommonDivisor(b, a % b)
}

fun leastCommonMultiple(input: List<Long>): Long {
    return input.reduce { acc, i -> leastCommonMultiple(acc, i) }
}

private fun stepsNeeded(
    instructions: Map<String, Instruction>,
    turns: List<Turn>,
    start: String
): Long {
    var steps = 0L
    var turnIndex = 0
    var location = start
    while (true) {
        if (location.endsWith("Z")) {
            return steps
        }

        val instruction = instructions[location]!!
        val turn = turns[turnIndex]
        location = if (turn == Turn.L) {
            instruction.left
        } else {
            instruction.right
        }

        steps++
        if (steps % 1000 == 0L) {
            println(steps)
        }

        turnIndex++
        if (turnIndex == turns.size) {
            turnIndex = 0
        }
    }
    throw IllegalStateException("Should not reach here")
}
