package year2024.day2024_21

import Direction
import Point
import puzzle
import java.util.concurrent.Future

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
    var current = keypadMoves(code, numericKeypad)
    (0 until 2).forEach {
        current = current.flatMap { keypadMoves(it, robotKeypad) }
    }
//    println("depressurized: $depressurized: ${depressurized}")
//    println("radiation: $radiation: ${radiation}")
//    println("cold: $cold: ${cold}")
//    println("minBy: ${cold.minOf { it.length }}")
    println("length: ${current.size}")
//    throw IllegalArgumentException("done")
    return current.minBy { it.length }
}

data class Move(val code: String, val location: Point, var result: List<String> = emptyList()) {
    fun get(): List<String> {
        // todo
        return onComplete(emptyList())
    }
}

fun keypadMoves(
    code: String,
    keypad: Keypad,
    start: Point = keypad.start(),
    cache: MutableMap<String, List<String>> = mutableMapOf()
): List<String> {
    if (code.isEmpty()) {
        throw IllegalArgumentException("Invalid code")
    }
    var result = emptyList<String>()
    val elements = Move(code, start) {
        result = it
        it
    }
    val open = mutableListOf(elements)
    while (open.isNotEmpty()) {
        val move = open.removeLast()
        val moves = cache.getOrPut(code) {
            movesNoCache(move.code, keypad, move.location, open)
            move.onComplete(emptyList())
        }
    }
    return result
}

private fun movesNoCache(
    code: String,
    keypad: Keypad,
    start: Point,
    open: MutableList<Move>
): () -> List<String> {
    val first = code.first()
    val location = keypad.locations[first] ?: throw IllegalArgumentException("Invalid code $first")
    val moves = moves(start, location, keypad)
    if (code.length == 1) {
        return { moves }
    }

    val move = Move(code.drop(1), location)
    val promises: List<Move> = moves.map { m ->
        open.add(move)
        move
    }

    return {
        val all = promises.flatMap { it.get() }
        if (keypad == robotKeypad) {
            listOf(all.minBy { it.length })
        } else {
            all.distinct()
        }
    }
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

