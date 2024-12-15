package year2023.day2023_12

import puzzle

enum class Status(val symbol: Char) {
    OPERATIONAL('.'),
    DAMAGED('#'),
    UNKNOWN('?');

    companion object {
        fun of(symbol: Char): Status = entries.single { it.symbol == symbol }
    }
}

val tryStates = listOf(Status.OPERATIONAL, Status.DAMAGED)

fun main() {
    puzzle(525152) { lines ->
        lines.sumOf { line ->
            val s = line.split(" ")
            val size = 5
            val s1 = List(size) { s[0] }.joinToString("?")
            val brokenRecord = s1
                .map { Status.of(it) }
            val s2 = List(size) { s[1] }.joinToString(",")
            val damagedLengths = s2
                .split(",").map { it.toInt() }
            println(brokenRecord.joinToString("") { it.symbol.toString() } + " " + damagedLengths)
            val arrangements = arrangementsCached(
                Arrangement(
                    brokenRecord,
                    emptyList(),
                    damagedLengths,
                    0
                )
            )
            println("$line = $arrangements")
            arrangements
        }
    }
}

data class CacheKey(
    val brokenRecord: List<Status>,
    val damagedLengths: List<Int>,
    val damagedSpan: Int
)

data class Arrangement(
    val brokenRecord: List<Status>,
    val fixedRecord: List<Status>,
    val damagedLengths: List<Int>,
    val damagedSpan: Int,
    val cache: MutableMap<CacheKey, Long> = mutableMapOf(),
)

fun arrangementsCached(a: Arrangement): Long {
    return a.cache.getOrPut(CacheKey(a.brokenRecord, a.damagedLengths, a.damagedSpan)) {
        arrangements(a)
    }
}

fun arrangements(a: Arrangement): Long {
    var damagedLengthsIndex = 0
    if (a.brokenRecord.isEmpty()) {
        if (a.damagedSpan > 0) {
            if (a.damagedSpan != a.damagedLengths.first()) {
                return 0
            }
            damagedLengthsIndex++
        }
        return if (damagedLengthsIndex == a.damagedLengths.size) {
//            println(a.fixedRecord.joinToString("") { it.symbol.toString() })
            1
        } else {
            0
        }
    }
    if (a.damagedLengths.isEmpty()) {
        return if (a.brokenRecord.none { it == Status.DAMAGED }
        ) {
            1
        } else {
            0
        }
    }

    val neededSpace = a.damagedLengths.subList(damagedLengthsIndex, a.damagedLengths.size)
        .sum() + a.damagedLengths.size - damagedLengthsIndex - 1 - a.damagedSpan
    if (a.brokenRecord.size < neededSpace) {
        return 0
    }

    return tryStatus(a.brokenRecord.first(), a)
}

private fun tryStatus(
    first: Status,
    a: Arrangement
): Long {
    return when (first) {
        Status.DAMAGED -> {
            if (a.damagedSpan >= a.damagedLengths.first()) {
                return 0
            }

            arrangementsCached(
                a.copy(
                    brokenRecord = a.brokenRecord.subList(1, a.brokenRecord.size),
                    fixedRecord = a.fixedRecord + Status.DAMAGED,
                    damagedSpan = a.damagedSpan + 1
                )
            )
        }

        Status.OPERATIONAL -> {
            val i = if (a.damagedSpan > 0) {
                val want = a.damagedLengths.first()
                if (want != a.damagedSpan) {
                    return 0
                }
                1
            } else {
                0
            }
            arrangementsCached(
                a.copy(
                    brokenRecord = a.brokenRecord.subList(1, a.brokenRecord.size),
                    damagedLengths = a.damagedLengths.subList(i, a.damagedLengths.size),
                    damagedSpan = 0,
                    fixedRecord = a.fixedRecord + Status.OPERATIONAL
                )
            )
        }
        // unknown
        else -> tryStates.sumOf { status ->
            tryStatus(status, a)
        }
    }
}

