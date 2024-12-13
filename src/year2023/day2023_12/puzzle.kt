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

val tryStatus = listOf(Status.OPERATIONAL, Status.DAMAGED)

fun main() {
    puzzle(21) { lines ->
        lines.sumOf { line ->
            val s = line.split(" ")
            val brokenRecord = s[0].map { Status.of(it) }
            val damagedLengths = s[1].split(",").map { it.toInt() }
            arrangements(brokenRecord, damagedLengths, 0)
        }
    }
}

fun arrangements(brokenRecord: List<Status>, damagedLengths: List<Int>, blockRecordBefore: Int): Long {
    val i = brokenRecord.drop(blockRecordBefore).indexOf(Status.UNKNOWN) + blockRecordBefore
    if (i < blockRecordBefore) {
        val valid = isValid(brokenRecord, damagedLengths)
        if (valid) {
            println(brokenRecord.joinToString("") { it.symbol.toString() } + " " + damagedLengths + " " + valid)
        }
        return if (valid) 1 else 0
    }
    return tryStatus.sumOf { status ->
        arrangements(updateRecord(brokenRecord, i, status), damagedLengths, i + 1)
    }
}

fun updateRecord(brokenRecord: List<Status>, i: Int, status: Status): List<Status> {
    return brokenRecord.toMutableList().apply {
        set(i, status)
    }
}

fun isValid(brokenRecord: List<Status>, damagedLengths: List<Int>): Boolean {
    val s = brokenRecord.map { it.symbol }.joinToString("")
    val list = s.split(".").filter { it.isNotEmpty() }.map { it.length }
    return list == damagedLengths
}
