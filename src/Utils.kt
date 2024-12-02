import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines().filter { it.isNotBlank() }

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

fun check(got: Int, expected: Int) {
    require(got == expected) { "Check failed: got $got, expected $expected" }
}

typealias Part = (List<String>) -> Int

fun puzzle(day: String, expected: Int, part: Part) {
    check(part(readInput("Day${day}_test")), expected)

    val input = readInput("Day${day}")
     part(input).println()
}

//fun puzzle(day: String, part1: Part, expectedPart1: Int, part2: Part, expectedPart2) {
//    check(part1(readInput("Day${day}_test")), expectedPart1)
//
//    val input = readInput("Day${day}")
//     part1(input).println()
//}
