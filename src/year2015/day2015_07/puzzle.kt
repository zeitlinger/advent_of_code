package year2015.day2015_07

import puzzle

//
//123 -> x
//456 -> y
//x AND y -> d
//x OR y -> e
//x LSHIFT 2 -> f
//y RSHIFT 2 -> g
//NOT x -> h
//NOT y -> i

fun main() {
    puzzle(456) { lines ->
        val signals = mutableMapOf<String, Int>()
        fun getSignal(wire: String): Int {
            return signals[wire] ?: 0
        }

        lines.forEach { instruction ->
            val (command, wire) = instruction.split(" -> ")
            when {
                command.toIntOrNull() != null -> signals[wire] = command.toInt()
                command.contains("AND") -> {
                    val (x, y) = command.split(" AND ")
                    signals[wire] = getSignal(x) and getSignal(y)
                }
                command.contains("OR") -> {
                    val (x, y) = command.split(" OR ")
                    signals[wire] = getSignal(x) or getSignal(y)
                }
                command.contains("LSHIFT") -> {
                    val (x, y) = command.split(" LSHIFT ")
                    signals[wire] = getSignal(x) shl y.toInt()
                }
                command.contains("RSHIFT") -> {
                    val (x, y) = command.split(" RSHIFT ")
                    signals[wire] = getSignal(x) shr y.toInt()
                }
                command.contains("NOT") -> {
                    val x = command.split("NOT ")[1]
                    signals[wire] = getSignal(x).inv() and 0xffff
                }
                else -> signals[wire] = getSignal(command)
            }
            println(instruction)
        }
        signals["a"]!!
    }
}
