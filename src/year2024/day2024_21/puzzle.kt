package year2024.day2024_21

import Direction
import Point
import bisectLargest
import puzzle
import kotlin.math.pow

data class Keypad(val locations: Map<Char, Point>) {
    fun start(): Point {
        return locations.entries.single { it.key == 'A' }.value
    }
}

val numericKeypad = Keypad(
    mapOf(
        '7' to Point(0, 0),
        '8' to Point(1, 0),
        '9' to Point(2, 0),
        '4' to Point(0, 1),
        '5' to Point(1, 1),
        '6' to Point(2, 1),
        '1' to Point(0, 2),
        '2' to Point(1, 2),
        '3' to Point(2, 2),
        '0' to Point(1, 3),
        'A' to Point(2, 3),
    )
)

val robotKeypad = Keypad(
    mapOf(
        '^' to Point(1, 0),
        'A' to Point(2, 0),
        '<' to Point(0, 1),
        'v' to Point(1, 1),
        '>' to Point(2, 1),
    )
)

data class RobotKeyCache(private val cache: MutableMap<Point, MutableMap<String, Pair<String, Point>>>) {
    // todo only need length
    fun get(from: Point, codePart: String): Pair<String, Point>? {
        return cache[from]?.get(codePart)
    }

    fun put(from: Point, to: Point, input: String, output: String) {
        cache.getOrPut(from) { mutableMapOf() }[input] = output to to
    }
}

private const val cacheSize = 2

fun main() {
//    sequenceLength("1")
    puzzle(126384) { lines ->
        lines.sumOf { code ->
            val robotKeypadCache = RobotKeyCache(mutableMapOf())
            // warm up cache
            robotKeypad.locations.entries.forEach { e ->
                val start = e.value
                println("add cache: ${e.key}")
                robotKeypad.locations.entries.forEach { entry ->
                    addCache(entry, "", "", start, robotKeypadCache, start, cacheSize)
                }
            }

            val s = sequenceLength(code, robotKeypadCache)
            val length = s.length
            val i = code.dropLast(1).toInt()
            println("$code: $s")
            println("$length * $i = ${length * i}")
            length * i
        }
    }
}

private fun addCache(
    entry: Map.Entry<Char, Point>,
    input: String,
    seq: String,
    from: Point,
    robotKeypadCache: RobotKeyCache,
    start: Point,
    iterations: Int
) {
    if (iterations == 0) {
        return
    }
    val to = entry.value
    val input1 = input + entry.key.toString()
    if (input1.endsWith("^^")
        || input1.endsWith("vv")
        || input1.endsWith("<<<")
        || input1.endsWith(">>>")
    ) {
        return
    }
    val seq1 = seq + moves(from, to, robotKeypad).first()
    robotKeypadCache.put(start, to, input1, seq1)
    robotKeypad.locations.entries.forEach { e ->
        addCache(e, input1, seq1, to, robotKeypadCache, start, iterations - 1)
    }
}

fun sequenceLength(code: String, robotKeypadCache: RobotKeyCache): String {
    var current = recursiveKeypadMoves(code, numericKeypad)
    var last = 0
    (0 until 25).forEach { i ->
        val d = 2.5.pow(i ).toDouble() * 14
        println("iteration: $i")
        println("current: ${current.map { it.length }}")
        println("d: $d")
        current = current.flatMap { robotKeypadMoves(it, robotKeypadCache) }
        val cur = current.minOf { it.length }
        if (last > 0) {
            println("ratio ${cur / last.toDouble()}")
        }
        last = cur
    }
//    println("depressurized: $depressurized: ${depressurized}")
//    println("radiation: $radiation: ${radiation}")
//    println("cold: $cold: ${cold}")
//    println("minBy: ${cold.minOf { it.length }}")
//    throw IllegalArgumentException("done")
    return current.minBy { it.length }
}

fun robotKeypadMoves(
    code: String,
    cache: RobotKeyCache,
): List<String> {
    var result = ""
    var i = 0L
    val start = robotKeypad.start()
    var location = start
    while (i < code.length) {
        val j = bisectLargest(i + 1..code.length.coerceAtMost(cacheSize)) {
            cache.get(location, code.substring(i.toInt(), it.toInt())) != null
        }.toInt()
        val p = cache.get(location, code.substring(i.toInt(), j)) ?: throw IllegalArgumentException("Invalid code")
        result += p.first
        location = p.second
//        cache.put(start, location, code.substring(0, j), result)
        i = j.toLong()
    }
    // crop return to A
    return listOf(result)
}

fun recursiveKeypadMoves(code: String, keypad: Keypad, start: Point = keypad.start()): List<String> {
    if (code.isEmpty()) {
        throw IllegalArgumentException("Invalid code")
    }
    val first = code.first()
    val location = keypad.locations[first] ?: throw IllegalArgumentException("Invalid code $first")
    val moves = moves(start, location, keypad)
    if (code.length == 1) {
        return moves
    }

    val flatMap = moves.flatMap { m ->
        recursiveKeypadMoves(code.drop(1), keypad, location).flatMap {
            val list = listOf(m + it)
            list
        }
    }
    return flatMap.distinct()
}

fun moves(start: Point, dst: Point, keypad: Keypad): List<String> {
    val dx = dst.x - start.x
    val dy = dst.y - start.y

    val horizontalMoves = if (dx > 0) {
        Direction.RIGHT.symbol.toString().repeat(dx)
    } else {
        Direction.LEFT.symbol.toString().repeat(-dx)
    }
    val verticalMoves = if (dy > 0) {
        Direction.DOWN.symbol.toString().repeat(dy)
    } else {
        Direction.UP.symbol.toString().repeat(-dy)
    }
    val moves = mutableListOf<String>()
    val horizontal = Point(start.x + dx, start.y)
    if (keypad.locations.values.contains(horizontal)) {
        moves.add(horizontalMoves + verticalMoves + "A")
    }
    val vertical = Point(start.x, start.y + dy)
    if (keypad.locations.values.contains(vertical)) {
        moves.add(verticalMoves + horizontalMoves + "A")
    }
    if (moves.isEmpty()) {
        throw IllegalArgumentException("Invalid move $start -> $dst")
    }
    return moves.distinct()
}

