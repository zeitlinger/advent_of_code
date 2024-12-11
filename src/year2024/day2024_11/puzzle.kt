package year2024.day2024_11

import puzzle

val lookup = mutableMapOf<Int, MutableMap<Long, Int>>()

fun main() {
    puzzle(null) { lines ->
        lines[0]
            .split(" ")
            .map { it.toLong() }
            .sumOf { expandWithCache(it, 1) }
    }
}

private fun expandWithCache(value: Long, start: Int): Int {
    if (start > 75) return 1

    if (start.mod(10) == 0) {
        val levelCache = lookup.getOrPut(start) { mutableMapOf() }
        return levelCache.getOrPut(value) { expand(start, value) }
    }

    return expand(start, value)
}

private fun expand(start: Int, value: Long): Int {
    val nextStart = start + 1

    if (value == 0L) return expandWithCache(1, nextStart)

    val s = value.toString()
    if (s.length.mod(2) == 0) {
        return expandWithCache(s.substring(0, s.length / 2).toLong(), nextStart) +
                expandWithCache(s.substring(s.length / 2).toLong(), nextStart)
    }

    return expandWithCache(value * 2024, nextStart)
}
