import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String, name: String, keepEmptyRows: Boolean) =
    Path("src/$day", name).readText().trim().lines().filter { keepEmptyRows || it.isNotBlank() }

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun check(got: String, expected: String) {
    require(got == expected) { "Check failed: got $got, expected $expected" }
}

data class PuzzleRun(val lines: List<String>, val test: Boolean) {
    fun lineBlocks(): List<List<String>> {
        val result = mutableListOf<List<String>>()
        var remaining = lines
        while (true) {
            val i = remaining.indexOf("")
            if (i == -1) {
                result.add(remaining)
                return result
            }
            result.add(remaining.subList(0, i))
            remaining = remaining.subList(i + 1, remaining.size)
        }
    }
}

fun <N : Number> puzzle(
    expected: N?,
    keepEmptyRows: Boolean = false,
    runTest: Boolean = true,
    part: (List<String>) -> N
) {
    stringPuzzle(expected?.toString(), keepEmptyRows, runTest, skip = 3) { run -> part(run.lines).toString() }
}

fun stringPuzzle(
    expected: String?,
    keepEmptyRows: Boolean = true,
    runTest: Boolean = true,
    skip: Long = 2,
    part: (PuzzleRun) -> String
) {
    val day = StackWalker.getInstance().walk { stack ->
        val caller = stack.skip(skip).findFirst().get()
        caller.className.split(".").take(2).joinToString("/")
    }
    if (expected != null && runTest) {
        val input = readInput(day, "test.txt", keepEmptyRows)
        check(part(PuzzleRun(input, true)), expected)
    }

    val duration = measureTime {
        val input = readInput(day, "input.txt", keepEmptyRows)
        println(part(PuzzleRun(input, false)))
    }
    println("Time: $duration")
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    fun move(direction: Direction): Point {
        return Point(x + direction.x, y + direction.y)
    }

    override fun compareTo(other: Point): Int {
        return when {
            y < other.y -> -1
            y > other.y -> 1
            x < other.x -> -1
            x > other.x -> 1
            else -> 0
        }
    }

    fun directionTo(point: Point): Direction {
        val dx = point.x - x
        val dy = point.y - y
        return if (dx == 0) {
            if (dy > 0) {
                Direction.DOWN
            } else {
                Direction.UP
            }
        } else {
            if (dx > 0) {
                Direction.RIGHT
            } else {
                Direction.LEFT
            }
        }
    }

    fun manhattanDistance(start: Point): Int {
        return abs(x - start.x) + abs(y - start.y)
    }
}

data class Visit(val point: Point, val direction: Direction)

enum class Direction(val x: Int, val y: Int, val symbol: Char) {
    UP(0, -1, '^'),
    DOWN(0, 1, 'v'),
    LEFT(-1, 0, '<'),
    RIGHT(1, 0, '>');

    fun turnLeft(): Direction {
        return when (this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }
    }

    fun turnRight(): Direction {
        return when (this) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
        }
    }

    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }

    companion object {
        fun of(c: Char): Direction {
            return entries.single { it.symbol == c }
        }
    }
}

fun bisectSmallest(range: LongRange, predicate: (Long) -> Boolean): Long {
    var left = range.first
    var right = range.last
    while (left < right) {
        val mid = left + (right - left) / 2
        if (predicate(mid)) {
            right = mid
        } else {
            left = mid + 1
        }
    }
    return left
}

fun bisectLargest(range: LongRange, predicate: (Long) -> Boolean): Long {
    var left = range.first
    var right = range.last
    while (left < right) {
        val mid = left + (right - left + 1) / 2
        val b = predicate(mid)
        if (b) {
            left = mid
        } else {
            right = mid - 1
        }
    }
    return left
}

fun <T> Collection<T>.permutations(): Set<List<T>> {
    if (this.isEmpty()) return emptySet()

    fun <T> _allPermutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<T>> = mutableSetOf()
        for (i in list.indices) {
            _allPermutations(list - list[i]).forEach { item ->
                result.add(item + list[i])
            }
        }
        return result
    }

    return _allPermutations(this.toList())
}
