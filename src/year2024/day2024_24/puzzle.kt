package year2024.day2024_24

import puzzle
import stringPuzzle

//tnw OR pbm -> gnj
val opRegex = Regex("""(\w+) (\w+) (\w+) -> (\w+)""")

//y04: 1
val valueRegex = Regex("""(\w+): (\d+)""")

fun main() {
    stringPuzzle(null) { input ->
        val wires = input.lines.map { instruction ->
            val literal = valueRegex.matchEntire(instruction)
            val op = opRegex.matchEntire(instruction)
            when {
                literal != null -> {
                    val (name, value) = literal.destructured
                    Wire(
                        name,
                        instruction,
                        value.toLong(),
                        emptyList()
                    ) { throw IllegalArgumentException("Should not be called") }
                }

                op != null -> {
                    val (x, op, y, name) = op.destructured
                    Wire(name, instruction, null, listOf(x, y)) { vals ->
                        when (op) {
                            "AND" -> vals[0] and vals[1]
                            "OR" -> vals[0] or vals[1]
                            "XOR" -> vals[0] xor vals[1]
                            else -> throw IllegalArgumentException("Unknown operation $op")
                        }
                    }
                }

                else -> {
                    val (name, x) = instruction.split(" -> ")
                    Wire(name, instruction, null, listOf(x)) { it[0] }
                }
            }
        }
        printTree(wires)
        simulateWires(wires)
        wireOutput(wires)
    }
}

fun printTree(wires: List<Wire>) {
    val wireMap = wires.associateBy { it.name }
    wires.filter { w -> wires.none { w.name in it.needs } }
        .sortedBy { it.name }
        .forEach { w -> printTree(w, wireMap, 0) }
}

fun printTree(wires: Wire, wireMap: Map<String, Wire>, level: Int) {
    println("${" ".repeat(level)}${wires.name} = ${wires.instruction}")
    wires.needs.forEach { wireMap[it]?.let { w -> printTree(w, wireMap, level + 1) } }
}

fun wireOutput(wires: List<Wire>): Long {
    return wires
        .filter { it.name.startsWith("z") }
        .sortedBy { it.name }
        .map {
            val order = it.name.substring(1).toInt()
            it.value!! shl order
        }
        .sum()
}


data class Wire(
    val name: String,
    val instruction: String,
    var value: Long?,
    val needs: List<String>,
    val operation: (List<Long>) -> Long
) {
    fun calculate(wires: Map<String, Wire>) {
        value = operation(needs.map { wires[it]!!.value!! })
    }
}

fun simulateWires(wireList: List<Wire>) {
    println("### start ###")

    val wires =
        wireList.associateBy { it.name }

//        val wires =
//            lines.map { instruction ->
//                val (command, wire) = instruction.split(" -> ")
//                when {
//                    command.toIntOrNull() != null ->
//                        Wire(
//                            wire,
//                            instruction,
//                            command.toInt(),
//                            emptyList(),
//                            { throw IllegalArgumentException("Should not be called") })
//
//                    command.contains("AND") -> {
//                        val (x, y) = command.split(" AND ")
//                        val l = literals(x, y)
//                        Wire(wire, instruction, null, wireNames(x, y)) {
//                            val vals = l + it
//                            vals[0] and vals[1]
//                        }
//                    }
//
//                    command.contains("OR") -> {
//                        val (x, y) = command.split(" OR ")
//                        val l = literals(x, y)
//                        Wire(wire, instruction, null, wireNames(x, y)) {
//                            val vals = l + it
//                            vals[0] or vals[1]
//                        }
//                    }
//
//                    command.contains("LSHIFT") -> {
//                        val (x, y) = command.split(" LSHIFT ")
//                        Wire(wire, instruction, null, listOf(x), { it[0] shl y.toInt() })
//                    }
//
//                    command.contains("RSHIFT") -> {
//                        val (x, y) = command.split(" RSHIFT ")
//                        Wire(wire, instruction, null, listOf(x), { it[0] shr y.toInt() })
//                    }
//
//                    command.contains("NOT") -> {
//                        val x = command.split("NOT ")[1]
//                        Wire(wire, instruction, null, listOf(x), { it[0].inv() and 0xffff })
//                    }
//
//                    else -> Wire(wire, instruction, null, listOf(command), { it[0] })
//                }
//            }.associateBy { it.name }

//        val inactive = wires.values.filter { it.value == null }.toMutableSet()

    while (true) {
        val wire = wires.values.firstOrNull { w -> w.value == null && w.needs.all { wires[it]!!.value != null } }
        if (wire == null) {
            break
        }
        wire.calculate(wires)
        println("${wire.instruction}: ${wire.name} = ${wire.value} (based on ${wire.needs.map { "$it=${wires[it]!!.value!!}" }})")
    }
}
