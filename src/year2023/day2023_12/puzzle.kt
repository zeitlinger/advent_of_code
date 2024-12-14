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
            val arrangements = arrangements(
                Arrangement(
                    brokenRecord,
                    emptyList(),
                    0,
                    damagedLengths,
                    0,
                    0
                )
            )
            println("$line = $arrangements")
            arrangements
        }
    }
}

data class Arrangement(
    val brokenRecord: List<Status>,
    val fixedRecord: List<Status>,
    val brokenRecordIndex: Int,
    val damagedLengths: List<Int>,
    val damagedLengthsIndex: Int,
    val damagedSpan: Int)

fun arrangements(a: Arrangement): Long {
    var damagedLengthsIndex = a.damagedLengthsIndex
    if (a.brokenRecordIndex == a.brokenRecord.size) {
        if (a.damagedSpan > 0) {
            if (a.damagedSpan != a.damagedLengths[damagedLengthsIndex]) {
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
    if (damagedLengthsIndex == a.damagedLengths.size) {
        return if (a.brokenRecord.subList(a.brokenRecordIndex, a.brokenRecord.size)
                .none { it == Status.DAMAGED }
        ) {
            1
        } else {
            0
        }
    }

    val neededSpace = a.damagedLengths.subList(damagedLengthsIndex, a.damagedLengths.size)
        .sum() + a.damagedLengths.size - damagedLengthsIndex - 1 - a.damagedSpan
    if (a.brokenRecord.size - a.brokenRecordIndex < neededSpace) {
        return 0
    }

    return tryStatus(a.brokenRecord[a.brokenRecordIndex], a)
}

private fun tryStatus(
    first: Status,
    a: Arrangement
): Long {
    return when (first) {
        Status.DAMAGED -> {
            if (a.damagedSpan >= a.damagedLengths[a.damagedLengthsIndex]) {
                return 0
            }

            arrangements(
                a.copy(
                    brokenRecordIndex = a.brokenRecordIndex + 1,
                    fixedRecord = a.fixedRecord + Status.DAMAGED,
                    damagedSpan = a.damagedSpan + 1
                )
            )
        }
        Status.OPERATIONAL -> {
            val i = if (a.damagedSpan > 0) {
                val want = a.damagedLengths[a.damagedLengthsIndex]
                if (want != a.damagedSpan) {
                    return 0
                }
               a.damagedLengthsIndex + 1
            } else {
                a.damagedLengthsIndex
            }
            arrangements(a.copy(
                brokenRecordIndex = a.brokenRecordIndex + 1,
                damagedLengthsIndex = i,
                damagedSpan = 0,
                fixedRecord = a.fixedRecord + Status.OPERATIONAL
                ))
        }
        // unknown
        else -> tryStates.sumOf { status ->
            tryStatus(status, a)
        }
    }
}

