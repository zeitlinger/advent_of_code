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

data class Wire(
    val name: String,
    val instruction: String,
    var value: Int?,
    val needs: List<String>,
    val operation: (List<Int>) -> Int
) {
    fun calculate(wires: Map<String, Wire>) {
        value = operation(needs.map { wires[it]!!.value!! })
    }
}

fun main() {
    puzzle(456) { lines ->
        println("### start ###")

        val wires =
            lines.map { instruction ->
                val (command, wire) = instruction.split(" -> ")
                when {
                    command.toIntOrNull() != null ->
                        Wire(
                            wire,
                            instruction,
                            command.toInt(),
                            emptyList(),
                            { throw IllegalArgumentException("Should not be called") })

                    command.contains("AND") -> {
                        val (x, y) = command.split(" AND ")
                        val l = literals(x, y)
                        Wire(wire, instruction, null, wireNames(x, y)) {
                            val vals = l + it
                            vals[0] and vals[1]
                        }
                    }

                    command.contains("OR") -> {
                        val (x, y) = command.split(" OR ")
                        val l = literals(x, y)
                        Wire(wire, instruction, null, wireNames(x, y)) {
                            val vals = l + it
                            vals[0] or vals[1]
                        }
                    }

                    command.contains("LSHIFT") -> {
                        val (x, y) = command.split(" LSHIFT ")
                        Wire(wire, instruction, null, listOf(x), { it[0] shl y.toInt() })
                    }

                    command.contains("RSHIFT") -> {
                        val (x, y) = command.split(" RSHIFT ")
                        Wire(wire, instruction, null, listOf(x), { it[0] shr y.toInt() })
                    }

                    command.contains("NOT") -> {
                        val x = command.split("NOT ")[1]
                        Wire(wire, instruction, null, listOf(x), { it[0].inv() and 0xffff })
                    }

                    else -> Wire(wire, instruction, null, listOf(command), { it[0] })
                }
            }.associateBy { it.name }

//        val inactive = wires.values.filter { it.value == null }.toMutableSet()

        while (true) {
            val wire = wires.values.firstOrNull { w -> w.value == null && w.needs.all { wires[it]!!.value != null } }
            if (wire == null) {
                break
            }
            wire.calculate(wires)
            println("${wire.instruction}: ${wire.name} = ${wire.value} (based on ${wire.needs.map { "$it=${wires[it]!!.value!!}" }})")
        }
        wires["a"]!!.value!!
    }
}

fun literals(x: String, y: String): List<Int> {
    val xInt = x.toIntOrNull()
    val yInt = y.toIntOrNull()
    if (yInt != null) {
        throw IllegalArgumentException("Not implemented")
    }
    return listOfNotNull(xInt, yInt)
}

fun wireNames(x: String, y: String): List<String> {
    return listOf(x, y).filter { it.toIntOrNull() == null }
}
