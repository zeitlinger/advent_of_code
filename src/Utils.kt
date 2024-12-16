import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String, name: String) = Path("src/$day", name).readText().trim().lines().filter { it.isNotBlank() }

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <N : Number> check(got: N, expected: N) {
    require(got == expected) { "Check failed: got $got, expected $expected" }
}

fun <N : Number> puzzle(expected: N?, part: (List<String>) -> N) {
    val day = StackWalker.getInstance().walk { stack ->
        val caller = stack.skip(1).findFirst().get()
        caller.className.split(".").take(2).joinToString("/")
    }
    if (expected != null) {
        check(part(readInput(day, "test.txt")), expected)
    }

    val duration = measureTime {
        val input = readInput(day, "input.txt")
        part(input).println()
    }
    println("Time: $duration")

}


data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction): Point {
        return Point(x + direction.x, y + direction.y)
    }
}

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


