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
    puzzle(126384L) { lines ->
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
    val depressurized = keypadMoves(code, numericKeypad)
    val radiation = keypadMoves(depressurized, robotKeypad)
    val cold = keypadMoves(radiation, robotKeypad)
    println("depressurized: $depressurized: ${depressurized.length}")
    println("radiation: $radiation: ${radiation.length}")
    println("cold: $cold: ${cold.length}")
    throw IllegalArgumentException("done")
    return cold
}

fun keypadMoves(code: String, keypad: Keypad, start: Point = keypad.start()): String {
    if (code.isEmpty()) {
        return ""
    }
    val first = code.first()
    val location = keypad.locations[first] ?: throw IllegalArgumentException("Invalid code $first")
    val moves = moves(start, location, keypad) + "A"
    if (moves.length < 2) {
//        throw IllegalArgumentException("Invalid move $start -> $location")
    }
    return moves + keypadMoves(code.drop(1), keypad, location)
}

fun moves(start: Point, dst: Point, keypad: Keypad): String {
    if (start == dst) {
        return ""
    }
    val distance = dst.manhattanDistance(start)
    return Direction.entries.mapNotNull { direction ->
        val p = start.move(direction)
        val possible = p.manhattanDistance(dst) < distance && keypad.locations.values.contains(p)
        if (possible) {
            val moves = moves(start.move(direction), dst, keypad)
            direction.symbol + moves
        } else {
            null
        }
    }.minBy { it.length }
}

