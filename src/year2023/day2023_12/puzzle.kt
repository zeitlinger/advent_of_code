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
            val s1 = List(5) { s[0] }.joinToString("?")
            val brokenRecord = s1
                .map { Status.of(it) }
            val s2 = List(5) { s[1] }.joinToString(",")
            val damagedLengths = s2
                .split(",").map { it.toInt() }
            println(brokenRecord.joinToString("") { it.symbol.toString() } + " " + damagedLengths)
            val arrangements = arrangements(brokenRecord, damagedLengths, 0)
            println("$line = $arrangements")
            arrangements
        }
    }
}

fun arrangements(brokenRecord: List<Status>, damagedLengths: List<Int>, damagedSpan: Int): Long {
    if (brokenRecord.isEmpty()) {
        return if (damagedLengths.isEmpty()) 1 else 0
    }
    if (damagedLengths.isEmpty()) {
        return if (brokenRecord.all { it == Status.OPERATIONAL }) 1 else 0
    }

    return tryStatus(brokenRecord.first(), brokenRecord, damagedLengths, damagedSpan)
}

private fun tryStatus(
    first: Status,
    brokenRecord: List<Status>,
    damagedLengths: List<Int>,
    damagedSpan: Int
): Long {
    return when (first) {
        Status.DAMAGED -> arrangements(brokenRecord.drop(1), damagedLengths, damagedSpan + 1)
        Status.OPERATIONAL -> when {
            damagedSpan > 0 && damagedLengths.first() != damagedSpan -> 0
            else -> arrangements(brokenRecord.drop(1), damagedLengths, 0)
        }
        // unknown
        else -> tryStates.sumOf { status ->
            tryStatus(status, brokenRecord, damagedLengths, damagedSpan)
        }
    }
}

