package year2024.day2024_21

import Direction
import Point
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
            val s = sequenceLength(code)
            val length = s.length
            val i = code.dropLast(1).toInt()
            println("$code: $s")
            println("$length * $i = ${length * i}")
            length * i
        }
    }
}

fun sequenceLength(code: String): String {
    var current = keypadMoves(code, numericKeypad, "numeric")
    (0 until 25).forEach { i ->
        println("iteration: $i")
        println("current: ${current.size}")
        current = current.flatMap { keypadMoves(it, robotKeypad, "robot $i") }
    }
//    println("depressurized: $depressurized: ${depressurized}")
//    println("radiation: $radiation: ${radiation}")
//    println("cold: $cold: ${cold}")
//    println("minBy: ${cold.minOf { it.length }}")
    println("length: ${current.size}")
//    throw IllegalArgumentException("done")
    return current.minBy { it.length }
}

data class Move(
    val code: String,
    val location: Point,
    val cache: MutableMap<String, List<String>>,
    var ready: Boolean = false,
    var notified: Boolean = false,
    val callback: () -> Unit,
) {
    fun ready() {
        ready = true
    }
    fun call() {
        if (ready && !notified) {
            callback()
            notified = true
        }
    }
}

fun keypadMoves(
    code: String,
    keypad: Keypad,
    name: String,
    start: Point = keypad.start(),
): List<String> {
    val cache = mutableMapOf<String, List<String>>()
    if (code.isEmpty()) {
        throw IllegalArgumentException("Invalid code")
    }
    val m = Move(code, start, cache) {
    }
    val open = mutableListOf(m)
    val closed = mutableListOf<Move>()
    while (open.isNotEmpty()) {
        val move = open.removeLast()
        moves(keypad, open, cache, move)
        closed.add(move)
    }
    closed.reversed().forEach { it.call() }
    return cache[code]!!
}

private fun moves(
    keypad: Keypad,
    open: MutableList<Move>,
    cache: MutableMap<String, List<String>>,
    move: Move
) {
    val code = move.code
    val start = move.location
    val first = code.first()
    val location = keypad.locations[first] ?: throw IllegalArgumentException("Invalid code $first")
    val moves = moves(start, location, keypad)
    if (code.length == 1) {
        cache[code] = moves
        move.ready()
        return
    }

    val next = code.drop(1)
    open.add(Move(next, location, cache) {
        val n = cache[next] ?: throw IllegalArgumentException("Invalid code $next")
        val all = moves.flatMap { m ->
            n.flatMap { listOf(m + it) }
        }
        val value = if (keypad == robotKeypad) {
            listOf(all.minBy { it.length })
        } else {
            all.distinct()
        }
        cache[code] = value
        move.ready()
    })
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

