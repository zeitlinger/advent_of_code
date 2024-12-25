package year2024.day2024_21

import Direction
import Point
import stringPuzzle

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

typealias LevelCache = MutableMap<Point, MutableMap<Char, Pair<Long, Point>>>

data class RobotKeyCache(private val cache: List<LevelCache>) {
    private fun levelCache(level: Int): LevelCache {
        return cache[level]
    }

    fun get(from: Point, code: Char, levelsRemaining: Int): Pair<Long, Point>? =
        levelCache(levelsRemaining)[from]?.get(code)

    fun put(from: Point, to: Point, input: Char, output: Long, levelsRemaining: Int) {
        levelCache(levelsRemaining).getOrPut(from) { mutableMapOf() }[input] = output to to
    }
}

fun main() {
    stringPuzzle("126384") { input ->
        val levels = if (input.test) 2 else 25
        input.lines.sumOf { code ->
            val robotKeypadCache = RobotKeyCache(List(levels) { mutableMapOf() })

            val moves = recursiveKeypadMoves(code, numericKeypad)
            val all = moves.map { robotKeypadMoves(it, robotKeypadCache, levels - 1) }
            val length = all.min()
            val i = code.dropLast(1).toInt()
            println("$length * $i = ${length * i}")
            length * i
        }.toString()
    }
}

fun robotKeypadMoves(
    code: String,
    cache: RobotKeyCache,
    levelsRemaining: Int,
): Long {
    var location = robotKeypad.start()
    var length = 0L
    code.forEach { c ->
        val p = cache.get(location, c, levelsRemaining)
        if (p != null) {
            length += p.first
            location = p.second
        } else {
            val to = robotKeypad.locations[c] ?: throw IllegalArgumentException("Invalid code $c")
            val moves = moves(location, to, robotKeypad)
            val l = moves.minOf { move ->
                if (levelsRemaining == 0) {
                    move.length.toLong()
                } else {
                    robotKeypadMoves(
                        move,
                        cache,
                        levelsRemaining - 1
                    )
                }
            }
            cache.put(location, to, c, l, levelsRemaining)
            location = to
            length += l
        }
    }
    return length
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

