package year2024.day2024_22

import puzzle

const val modulo = 2.shl(23).toLong()

data class Random(var value: Long) {
    fun next(): Long {
        generate(value.shl(6))
        generate(value.shr(5))
        generate(value.shl(11))
        return value
    }

    private fun generate(factor: Long) {
        val xor = value.xor(factor)
        val mod = xor.mod(modulo)
//        println("value: $value, factor: $factor xor: $xor mod: $mod")
        value = mod
    }
}

fun main() {
    puzzle(37327623) { lines ->
        lines.sumOf {
            val random = Random(it.toLong())
            var value = 0L
            for (i in 0 until 2000) {
                value = random.next()
            }
            println(value)
            value
        }
    }
}
