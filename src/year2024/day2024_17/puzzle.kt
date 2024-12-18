package year2024.day2024_17

import puzzle

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
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    val program: List<Int>,
    val output: MutableList<Int>,
    var jumpCounter: Int = 0
) {
    private fun combo(): Long {
        val literal = literal()
        if (literal < 0 || literal > 6) {
            throw IllegalArgumentException("Invalid combo")
        }
        if (literal < 4) {
            return literal.toLong()
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
                registerB = registerB.xor(literal().toLong())
            }

            Instruction.bxc -> {
                registerB = registerB.xor(registerC)
            }

            Instruction.bst -> {
                registerB = combo().and(7)
            }

            Instruction.out -> {
                output.add(combo().and(7L).toInt())
            }

            Instruction.jnz -> {
                if (registerA != 0L) {
                    instructionPointer = literal()
                    advance = false

                    jumpCounter++
                    if (jumpCounter > program.size) {
                        println(this)
                        throw IllegalStateException("Jump counter exceeded")
                    }
                }
            }
        }
        println("${instruction.name}: combo=${combo()} a=${registerA.binary()} b=${registerB.binary()} c=${registerC.binary()}")
        if (advance) {
            instructionPointer += 2
        }
    }

    private fun div(): Long {
        return registerA.shr(combo().toInt())
    }

}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    puzzle(117440) { lines ->
        val m = lines.associate {
            val s = it.split(":")
            s[0] to s[1].trim()
        }
        val want = m.getValue("Program").split(",").map { it.toInt() }
        val original = Cpu(
            0,
            m.getValue("Register A").toLong(),
            m.getValue("Register B").toLong(),
            m.getValue("Register C").toLong(),
            want,
            mutableListOf()
        )
//        var currentMask = 4L
//        for (mask in 0..7) {
        val registerA = nextDigit(0, original, original.program.size - 1)
        //            if (registerA == 117440) {
        println("Register A: $registerA")
        println("Register A: ${registerA.binary()}")
        //                println("Output: $output")
        //            }

//            if (registerA % 10000000 == 0) {
//                println("Register A: $registerA")
//            }
        throw IllegalStateException("No solution found")
    }
}

private fun Long.binary(): String = java.lang.Long.toBinaryString(this)

private fun nextDigit(
    lastRegisterA: Long,
    original: Cpu,
    i: Int
): Long {
    for (current in 0..7) {
//        var registerA1 = registerA
//        val reversed = program.reversed()
//        reversed.forEachIndexed { index, i ->
//            if (index > 0) {
//                    registerA = registerA.xor(5)
        val registerA = lastRegisterA.shl(3).or(current.toLong())
//            }

//            val last = if (index == 0) 0 else reversed[index - 1].toLong()
//            val shift = i.toLong().xor(3)
//            val l = shift.xor(last).xor(5)
//            registerA1 = registerA1.or(l)

//        println("Register A: ${java.lang.Long.toBinaryString(registerA1)}")
        val cpu = original.copy(output = mutableListOf(), registerA = registerA)
        println("Try: ${registerA.binary()}")
        println(cpu)
        val output = execute(cpu)
        val want = original.program[i]
        if (output.first() == want) {
            println("Found: ${registerA.binary()}")
            if (i == 0) {
                return registerA
            }
            return nextDigit(registerA, original, i - 1)
        }
    }
    throw IllegalStateException("No solution found")
}

private fun execute(cpu: Cpu): List<Int> {
    while (cpu.instructionPointer < cpu.program.size) {
        val instruction = Instruction.of(cpu.program[cpu.instructionPointer])
        cpu.execute(instruction)
    }
    return cpu.output
}
