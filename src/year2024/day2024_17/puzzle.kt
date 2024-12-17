package year2024.day2024_17

import stringPuzzle
import kotlin.math.pow

enum class Instruction {
    adv,
    bxl,
    bst,
    jnz,
    bxc,
    out,
    bdv,
    cdv;

    companion object {
        fun of(c: Int): Instruction = Instruction.entries.singleOrNull { it.ordinal == c }
            ?: throw IllegalArgumentException("Invalid instruction: $c")
    }
}

data class Cpu(
    var instructionPointer: Int,
    var registerA: Int,
    var registerB: Int,
    var registerC: Int,
    val program: List<Int>,
    val output: MutableList<Int>,
) {
    private fun combo(): Int {
        val literal = literal()
        if (literal < 0 || literal > 6) {
            throw IllegalArgumentException("Invalid combo")
        }
        if (literal < 4) {
            return literal
        }
        return when (literal) {
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw IllegalArgumentException("Invalid combo")
        }
    }

    private fun literal(): Int {
        return program[instructionPointer + 1]
    }

    fun execute(instruction: Instruction) {
        var advance = true
        when (instruction) {
            Instruction.adv -> {
                registerA = div()
            }

            Instruction.bdv -> {
                registerB = div()
            }

            Instruction.cdv -> {
                registerC = div()
            }

            Instruction.bxl -> {
                registerB = registerB.xor(literal())
            }

            Instruction.bxc -> {
                registerB = registerB.xor(registerC)
            }

            Instruction.bst -> {
                registerB = combo().mod(8)
            }

            Instruction.out -> {
                output.add(combo().mod(8))
            }

            Instruction.jnz -> {
                if (registerA != 0) {
                    instructionPointer = literal()
                    advance = false
                }
            }
        }
        if (advance) {
            instructionPointer += 2
        }
    }

    private fun div(): Int {
        val numerator = registerA
        val denominator = 2.0.pow(combo())
        val i = (numerator / denominator).toInt()
        return i
    }

}

fun main() {
    stringPuzzle("4,6,3,5,6,3,5,2,1,0") { lines ->
        val m = lines.associate {
            val s = it.split(":")
            s[0] to s[1].trim()
        }
        val cpu = Cpu(
            0,
            m.getValue("Register A").toInt(),
            m.getValue("Register B").toInt(),
            m.getValue("Register C").toInt(),
            m.getValue("Program").split(",").map { it.toInt() },
            mutableListOf())
        while (cpu.instructionPointer < cpu.program.size) {
            val instruction = Instruction.of(cpu.program[cpu.instructionPointer])
            cpu.execute(instruction)
        }
        cpu.output.joinToString(",")
    }
}
