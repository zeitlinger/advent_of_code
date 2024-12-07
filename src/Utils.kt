import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

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

fun <N: Number> check(got: N, expected: N) {
    require(got == expected) { "Check failed: got $got, expected $expected" }
}

fun <N: Number> puzzle(expected: N, part: (List<String>) -> N) {
    val day = StackWalker.getInstance().walk { stack ->
        val caller = stack.skip(1).findFirst().get()
        caller.className.split(".").take(2).joinToString("/")
    }
    check(part(readInput(day, "test.txt")), expected)

    val input = readInput(day, "input.txt")
    part(input).println()
}

