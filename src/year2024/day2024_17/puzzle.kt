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
    val output: MutableList<Long>,
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
                output.add(combo().and(7L))
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
//        for (mask in 0..7) {
        var registerA = 0L
        var mask = 4L
        original.program.reversed().forEachIndexed { index, i ->
            if (index > 0) {
                registerA = registerA.shl(3)
            }
            registerA = registerA.or(i.toLong().xor(mask))
            mask = mask.xor(5)
        }

        println("Register A: ${java.lang.Long.toBinaryString(registerA)} for mask $mask")
        val cpu = original.copy(output = mutableListOf(), registerA = registerA.toLong())
        val output = execute(cpu)
        //            if (registerA == 117440) {
        //                println("Register A: $registerA")
        //                println("Output: $output")
        //            }
        println(cpu)
        if (output == want) {
            println("Found: $registerA")
            return@puzzle registerA
        }
//        }
//            if (registerA % 10000000 == 0) {
//                println("Register A: $registerA")
//            }
        throw IllegalStateException("No solution found")
    }
}

private fun execute(cpu: Cpu): List<Long> {
    while (cpu.instructionPointer < cpu.program.size) {
        val instruction = Instruction.of(cpu.program[cpu.instructionPointer])
        cpu.execute(instruction)
    }
    return cpu.output
}
