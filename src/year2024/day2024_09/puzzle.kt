package year2024.day2024_09

import puzzle

fun main() {
    puzzle(2858) { lines ->
        val input = lines[0]
        val disk = mutableListOf<Int>()
        readDisk(input, disk)
        var maxFileId = disk.max()
        while (maxFileId >= 0) {
            val move = defrag(disk, maxFileId) ?: break
            maxFileId = move.fileId - 1
        }

        checksum(disk)
    }
}

fun checksum(disk: MutableList<Int>): Long = disk.mapIndexed { pos, id ->
    if (id == free) {
        0
    } else {
        id.toLong() * pos
    }
}.sum()

const val free = -1

data class Move(val from: Int, val to: Int, val len: Int, val fileId: Int)

fun defrag(disk: MutableList<Int>, maxFileId: Int): Move? {
    val move = getLastFreeToMove(disk, maxFileId) ?: return null
    val from = move.from
    val to = move.to
    val len = move.len
    for (i in len - 1 downTo 0) {
        disk[to + i] = disk[from + i]
        disk[from + i] = free
    }
//    println("Move file ${move.fileId} from $from to $to")
//    println(disk.map { if (it == free) "." else it }.joinToString(""))
    return move
}

private fun getLastFreeToMove(disk: MutableList<Int>, maxFileId: Int): Move? {
    if (maxFileId < 0) {
        return null
    }
    val start = disk.indexOf(maxFileId)
    val end = disk.lastIndexOf(maxFileId)
    val len = end - start + 1
    getFreeSpace(disk, len, start)?.let { return Move(start, it, len, maxFileId) }
    return getLastFreeToMove(disk, maxFileId - 1)
}

fun getFreeSpace(disk: MutableList<Int>, need: Int, before: Int): Int? {
    var startPos = 0
    var freeCount = 0
    disk.forEachIndexed { index, id ->
        if (index == before) {
            return null
        }
        if (id == free) {
            freeCount++
            if (freeCount >= need) {
                return startPos
            }
        } else {
            freeCount = 0
            startPos = index + 1
        }
    }
    return null
}

private fun readDisk(input: String, disk: MutableList<Int>) {
    for ((fileId, s) in input.chunked(2).withIndex()) {
        val fileLen = s[0].digitToInt()
        for (i in 0 until fileLen) {
            disk.add(fileId)
        }
        if (s.length == 1) {
            continue
        }
        val freeLen = s[1].digitToInt()
        for (i in 0 until freeLen) {
            disk.add(free)
        }
    }
}
