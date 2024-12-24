package year2024.day2024_24

import PuzzleRun
import stringPuzzle

data class Wire(
    val name: String,
    val instruction: String,
    val op: String,
    var value: Long?,
    val needs: List<String>,
    val operation: (List<Long>) -> Long
) {
    fun calculate(wires: Map<String, Wire>) {
        value = operation(needs.map { wires[it]!!.value!! })
    }
}

data class Circuit(
    val wires: Map<String, Wire>,
    val swaps: MutableMap<String, String>,
)

//tnw OR pbm -> gnj
val opRegex = Regex("""(\w+) (\w+) (\w+) -> (\w+)""")

//y04: 1
val valueRegex = Regex("""(\w+): (\d+)""")

fun main() {
    stringPuzzle(null) { input ->
        val wires = readInput(input)
        val circuit = Circuit(wires.associateBy { it.name }, mutableMapOf())
//        printTree(wires)
        fixWires(circuit)
//        simulateWires(wires)
//        wireOutput(wires)
        "todo"
    }
}

private fun readInput(input: PuzzleRun) = input.lines.map { instruction ->
    val literal = valueRegex.matchEntire(instruction)
    val operation = opRegex.matchEntire(instruction)
    when {
        literal != null -> {
            val (name, value) = literal.destructured
            Wire(
                name,
                instruction,
                "=",
                value.toLong(),
                emptyList()
            ) { throw IllegalArgumentException("Should not be called") }
        }

        operation != null -> {
            val (x, op, y, name) = operation.destructured
            Wire(name, instruction, op, null, listOf(x, y)) { vals ->
                when (op) {
                    "AND" -> vals[0] and vals[1]
                    "OR" -> vals[0] or vals[1]
                    "XOR" -> vals[0] xor vals[1]
                    else -> throw IllegalArgumentException("Unknown operation $op")
                }
            }
        }

        else -> throw IllegalArgumentException("Unknown instruction $instruction")
    }
}

fun fixWires(circuit: Circuit) {
    traceWire(0, circuit)
}

fun traceWire(bit: Int, circuit: Circuit, carryIn: Wire? = null) {
    println("trace bit: $bit")
    val wireMap = circuit.wires

    val num = bit.toString().padStart(2, '0')
    val resultName = "z$num"
    if (bit == 45) {
        // todo special case for bit 45
    } else {
        val x = wireMap["x$num"]!!
        val y = wireMap["y$num"]!!
        val inOxr = find(circuit, "XOR", x, y, null)
        val inAnd = find(circuit, "AND", x, y, null)
        val result =
            if (carryIn != null) {
                find(circuit, "XOR", inOxr, carryIn, resultName)
            } else {
                requireName(inOxr, resultName)
                inOxr
            }

        val carryOut: Wire =
            if (carryIn != null) {
                val carryAnd = find(circuit, "AND", carryIn, inOxr, null)
                val carryOr = find(circuit, "OR", inAnd, carryAnd, null)
                carryOr
            } else {
                inAnd
            }
        traceWire(bit + 1, circuit, carryOut)
    }
}

fun requireName(wire: Wire, resultName: String) {
    if (wire.name != resultName) {
        throw IllegalArgumentException("expected $resultName but got ${wire.name}")
    }
}

private fun find(
    circuit: Circuit,
    op: String,
    x: Wire,
    y: Wire,
    needName: String?
): Wire {
    val wireMap = circuit.wires
    val wire = wireMap.values.find {
        it.op == op && it.needs.contains(x.name) && it.needs.contains(y.name)
    }
    if (wire == null) {
        throw IllegalArgumentException("missing $op for ${x.name} and ${y.name}")
    }
    if (needName != null && wire.name != needName) {
        throw IllegalArgumentException("expected $needName but got ${wire.name}")
    }
    return wire
}

fun printTree(circuit: Circuit) {
    val wires = circuit.wires.values
    val wireMap = wires.associateBy { it.name }
    wires.filter { w -> wires.none { w.name in it.needs } }
        .sortedBy { it.name }
        .forEach { w -> printTree(w, circuit, 0) }
}

fun printTree(wires: Wire, circuit: Circuit, level: Int) {
    println("${" ".repeat(level)}${wires.name} = ${wires.instruction}")
    val wireMap = circuit.wires
    wires.needs.forEach { wireMap[it]?.let { w -> printTree(w, circuit, level + 1) } }
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

fun simulateWires(wireList: List<Wire>) {
    println("### start ###")

    val wires =
        wireList.associateBy { it.name }

    while (true) {
        val wire = wires.values.firstOrNull { w -> w.value == null && w.needs.all { wires[it]!!.value != null } }
        if (wire == null) {
            break
        }
        wire.calculate(wires)
        println("${wire.instruction}: ${wire.name} = ${wire.value} (based on ${wire.needs.map { "$it=${wires[it]!!.value!!}" }})")
    }
}
