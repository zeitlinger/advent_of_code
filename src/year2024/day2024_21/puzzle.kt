package year2024.day2024_21

import Direction
import Point
import bisectLargest
import puzzle

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

fun main() {
//    sequenceLength("1")
    puzzle(126384) { lines ->
        lines.sumOf { code ->
            val robotKeypadCache = mutableMapOf<String, String>()
            // warm up cache
            robotKeypad.locations.keys.forEach { key ->
                val keyLocation = robotKeypad.locations[key] ?: throw IllegalArgumentException("Invalid key")
                val toKey = moves(robotKeypad.start(), keyLocation, robotKeypad).first()
                val back = moves(keyLocation, robotKeypad.start(), robotKeypad).first()
                robotKeypadCache[key.toString()] = toKey + back
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

fun sequenceLength(code: String, robotKeypadCache: MutableMap<String, String>): String {
    var current = recursiveKeypadMoves(code, numericKeypad)
    (0 until 25).forEach { i ->
        println("iteration: $i")
        println("current: ${current.map { it.length }}")
        current = current.flatMap { robotKeypadMoves(it, robotKeypadCache) }
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
    cache: MutableMap<String, String>,
): List<String> {
    val result = mutableListOf<String>()
    var i = 0L
    while (i < code.length) {
        val j = bisectLargest(i + 1..code.length) {
            cache[code.substring(i.toInt(), it.toInt())] != null
        }
        val s = cache[code.substring(i.toInt(), j.toInt())] ?: throw IllegalArgumentException("Invalid code")
        result.add(s)
        i = j
    }
    // crop return to A
    return result
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

