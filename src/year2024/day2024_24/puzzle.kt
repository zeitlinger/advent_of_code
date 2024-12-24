package year2024.day2024_24

import PuzzleRun
import stringPuzzle

//tnw OR pbm -> gnj
val opRegex = Regex("""(\w+) (\w+) (\w+) -> (\w+)""")

//y04: 1
val valueRegex = Regex("""(\w+): (\d+)""")

fun main() {
    stringPuzzle(null) { input ->
        val wires = readInput(input)
//        printTree(wires)
        fixWires(wires)
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

fun fixWires(wires: List<Wire>) {
    val wireMap = wires.associateBy { it.name }
    traceWire(0, wireMap)
}

fun traceWire(bit: Int, wireMap: Map<String, Wire>) {
    println("trace bit: $bit")
    if (bit > 0) {
        // todo find carry-in
    }
    val num = bit.toString().padStart(2, '0')
    if (bit < 45) {
        // todo special case
        val x = wireMap["x$num"]!!
        val y = wireMap["y$num"]!!
        val inOxr = wireMap.values.find { it.op == "XOR" && it.needs.contains(x.name) && it.needs.contains(y.name) }!!
    }
    if (bit < 45) {
        traceWire(bit + 1, wireMap)
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
    val op: String,
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

    while (true) {
        val wire = wires.values.firstOrNull { w -> w.value == null && w.needs.all { wires[it]!!.value != null } }
        if (wire == null) {
            break
        }
        wire.calculate(wires)
        println("${wire.instruction}: ${wire.name} = ${wire.value} (based on ${wire.needs.map { "$it=${wires[it]!!.value!!}" }})")
    }
}
