package year2024.day2024_11

import puzzle

fun main() {
    puzzle(null) { lines ->
        lines[0]
            .split(" ")
            .map { it.toLong() }
            .sumOf { expandStones(it, 1) }
    }
}

private fun expandStones(value: Long, start: Int): Int {
    if (start > 75) return 1

    val nextStart = start + 1

    if (value == 0L) return expandStones(1, nextStart)

    val s = value.toString()
    if (s.length.mod(2) == 0) {
        return expandStones(s.substring(0, s.length / 2).toLong(), nextStart) +
                expandStones(s.substring(s.length / 2).toLong(), nextStart)
    }

    return expandStones(value * 2024, nextStart)
}
