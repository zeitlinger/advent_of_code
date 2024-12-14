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

fun arrangements(brokenRecord: List<Status>, damagedLengths: List<Int>, blockRecordBefore: Int): Long {
    val i = brokenRecord.drop(blockRecordBefore).indexOf(Status.UNKNOWN) + blockRecordBefore
    if (i < blockRecordBefore) {
        // all unknowns are filled

        val list = operationalBlocks(brokenRecord)
        val valid = list == damagedLengths
        if (valid) {
//            println(brokenRecord.joinToString("") { it.symbol.toString() } + " " + damagedLengths + " " + valid)
        }
        return if (valid) 1 else 0
    }
    // check all blocked records
    if (blockRecordBefore > 0) {
        if (!mayBeValid(brokenRecord, blockRecordBefore, damagedLengths)) {
            return 0
        }
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

fun mayBeValid(
    brokenRecord: List<Status>,
    blockRecordBefore: Int,
    damagedLengths: List<Int>
): Boolean {
    val subList = brokenRecord.subList(0, blockRecordBefore)
    if (subList.last() != Status.OPERATIONAL) {
        return true
    }

    val list = operationalBlocks(subList)
    if (!damagedLengths.startsWith(list)) {
        return false
    }
    val remaining = damagedLengths.drop(list.size)
    val minSize = remaining.sum() + remaining.size - 1
    return minSize <= brokenRecord.size - blockRecordBefore
}

fun operationalBlocks(brokenRecord: List<Status>): List<Int> {
    return brokenRecord.map { it.symbol }.joinToString("").split(".").filter { it.isNotEmpty() }.map { it.length }
}

private fun <E> List<E>.startsWith(list: List<E>): Boolean {
    if (size < list.size) {
        return false
    }
    return list.withIndex().all { (i, e) -> this[i] == e }
}
