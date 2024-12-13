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
    val part1 = part(readInput(day, "test.txt"))
    if (expected != null) {
        check(part1, expected)
    }

    val duration = measureTime {
        val input = readInput(day, "input.txt")
        part(input).println()
    }
    println("Time: $duration")

}

