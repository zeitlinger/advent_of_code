package year2024.day2024_21

import Direction
import Point
import stringPuzzle

data class Keypad(val locations: Map<Char, Point>) {
    fun start(): Point {
        return locations.entries.single { it.key == 'A' }.value
    }

    fun pointToIndex(point: Point): Int {
        return locations.entries.mapIndexedNotNull { index, entry -> if (entry.value == point) index else null }
            .single()
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

data class RobotNavigator(
    val current: Int,
    var next: List<Pair<RobotNavigator, List<Int>>>,
) {
    fun next(key: Int): Pair<RobotNavigator, List<Int>> {
        return next[key]
    }

    override fun toString(): String {
        return "RobotNavigator(current=$current)"
    }
}

fun main() {
//    sequenceLength("1")
    stringPuzzle("205160") { run ->
        val robotKeypadCache = createNavigator()
        val iterations = if (run.test) 2 else 25
        run.lines.sumOf { code ->
            val length = sequenceLength(code, robotKeypadCache, iterations)

            val i = code.dropLast(1).toInt()
            println("$length * $i = ${length * i}")
            length * i
        }.toString()
    }
}

private fun createNavigator(): RobotNavigator {
//    val next = mutableMapOf<Int, Pair<RobotNavigator, List<Int>>>()
    val all = mutableMapOf<Int, List<Pair<Int, List<Int>>>>()
//    val robotKeypadCache = RobotNavigator(mutableMapOf())
    // warm up cache
    robotKeypad.locations.entries.forEach { e ->
        val start = e.value
        val startIndex = robotKeypad.pointToIndex(start)
        println("add cache: ${e.key}")
        val pairs = robotKeypad.locations.entries.map { entry ->
            val end = entry.value
            val endIndex = robotKeypad.pointToIndex(end)
            val movesToIndexes = movesToIndexes(moves(start, end, robotKeypad).first())
            endIndex to movesToIndexes

//            addCache(entry, "", "", start, robotKeypadCache, start, 1)
        }
        all[startIndex] = pairs
    }
    val list = all.keys.map { RobotNavigator(it, emptyList()) }
    list.forEach { navigator ->
        val pairs = all[navigator.current]!!
        navigator.next = pairs.map { (next, moves) ->
            list[next] to moves
        }
    }
    return list[robotKeypad.pointToIndex(robotKeypad.start())]
}

data class Clue(val code: String, val min: Int, val copies: Int, val use: Int)

val clues = listOf(
    Clue("839A", 16,4, 1),
    Clue("169A", 14, 2, 0),
    Clue("579A", 14, 4, 0),
    Clue("670A", 14, 2, 0),
    Clue("638A", 14, 4, 1),
)

private fun numericMoves(code: String): String {
    val moves = recursiveKeypadMoves(code, numericKeypad)
    val l = moves.map { it.length }
    val clue = clues.single { it.code == code }
    val min = l.min()!!
    if (min != clue.min) {
        throw IllegalArgumentException("Invalid clue for $code: min is ${clue.min} but got $min")
    }
    if (moves.any { it.length != min }) {
        throw IllegalArgumentException("Invalid clue for $code: min is $min but got $moves")
    }
    val count = l.size
    if (count != clue.copies) {
        throw IllegalArgumentException("Invalid clue for $code: copies is ${clue.copies} but got $count")
    }
    return moves[clue.use]
}

fun sequenceLength(code: String, robotKeypadCache: RobotNavigator, iterations: Int): Long {
    val recursiveKeypadMoves = numericMoves(code)
    var iterator = movesToIndexes(recursiveKeypadMoves).iterator()
    (0 until iterations).forEach { i ->
        println("iteration: $i")
        iterator = robotKeypadMoves(robotKeypadCache, iterator, i)
    }
    var count = 0L
    while (iterator.hasNext()) {
        count++
        iterator.next()
        if (count % 100_0000_000L == 0L) {
            println("count: $count")
        }
    }
    println("count: $count")
    return count
}

private fun movesToIndexes(recursiveKeypadMoves: String): List<Int> = recursiveKeypadMoves.map {
    robotKeypad.pointToIndex(robotKeypad.locations.entries.single { e -> e.key == it }.value)
}

fun robotKeypadMoves(
    start: RobotNavigator,
    targetKeypad: Iterator<Int>,
    level: Int,
): Iterator<Int> {
    var navigator = start
    return iterator {
        var count = 0L
        while (targetKeypad.hasNext()) {
            count++
            if (level < 5) {
                println("move on level: $level")
            }
            val input = targetKeypad.next()
            val next = navigator.next(input)
            navigator = next.first
            yieldAll(next.second)
        }
        println("level: $level count: $count")
    }
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

