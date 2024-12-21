package year2024.day2024_21

import Direction
import Point
import puzzle

data class Robot(val position: Point, val openMoves: Set<Direction>, val next: Robot?)

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
        '^' to Point(0, 0),
        'A' to Point(1, 0),
        '<' to Point(0, 1),
        'v' to Point(1, 1),
        '>' to Point(1, 2),
    )
)

fun main() {
    puzzle(126384L) { lines ->
        lines.sumOf { sequenceLength(it) * it.dropLast(1).toInt() }
    }
}

fun sequenceLength(code: String): Long {
    val moves = keypadMoves(numericKeypad.start(), code, numericKeypad)
    println(moves.map { it.symbol }.joinToString(""))
    return 1
}

fun keypadMoves(start: Point, code: String, keypad: Keypad): List<Direction> {
    if (code.isEmpty()) {
        return emptyList()
    }
    val first = code.first()
    val location = keypad.locations[first] ?: throw IllegalArgumentException("Invalid code $first")
    val moves = moves(start, location)
    return moves + keypadMoves(location, code.drop(1), keypad)
}

fun moves(start: Point, dst: Point): List<Direction> {
    val dx = dst.x - start.x
    val dy = dst.y - start.y

    val horizontalMoves = if (dx > 0) {
        List(dx) { Direction.RIGHT }
    } else {
        List(-dx) { Direction.LEFT }
    }
    val verticalMoves = if (dy > 0) {
        List(dy) { Direction.DOWN }
    } else {
        List(-dy) { Direction.UP }
    }
    return horizontalMoves + verticalMoves
}

