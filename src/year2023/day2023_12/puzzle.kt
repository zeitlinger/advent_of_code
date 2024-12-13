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
    var damaged = 0
    var status = Status.OPERATIONAL
    val iterator = damagedLengths.iterator()
    for (i in brokenRecord.indices) {
        val next = brokenRecord[i]
        if (next != status) {
            when (next) {
                Status.OPERATIONAL -> {
                    if (!iterator.hasNext()) {
                        return false
                    }
                    val want = iterator.next()
                    if (damaged != want) {
                        return false
                    }
                }

                Status.DAMAGED -> {
                    damaged = 0
                }

                else -> throw IllegalArgumentException("Invalid status")
            }
        }

        if (next == Status.DAMAGED) {
            damaged++
        }

        status = next
    }
    if (iterator.hasNext()) {
        return damaged == iterator.next()
    }
    return damaged == 0
}
