package year2024.day07

import puzzle

fun main() {
    puzzle(3749) { lines ->
        lines.sumOf {
            // "190: 10 19" is the input: 190 is the test value, 10 and 19 the input values
            val (a, b) = it.split(":")
            val test = a.trim().toLong()
            val input = b.trim().split(" ").map { it.trim().toLong() }
            if (canSolve(test, input)) test else 0
        }
    }
}

enum class Operator(val apply: (Long, Long) -> Long) {
    PLUS(
        { a, b -> a + b }
    ),
    TIMES(
        { a, b -> a * b }
    ),
}

fun canSolve(want: Long, current: Long, left: List<Long>): Boolean {
    if (left.isEmpty()) return current == want
    return Operator.entries.any { canSolve(want, it.apply(current, left[0]), left.drop(1)) }
}

fun canSolve(test: Long, input: List<Long>): Boolean {
    return canSolve(test, input[0], input.drop(1))
}
