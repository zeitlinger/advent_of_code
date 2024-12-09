package year2024.day2024_09

import puzzle

fun main() {
    puzzle(1928) { lines ->
        val input = lines[0]
        val disk = mutableListOf<Int>()
        readDisk(input, disk)
        defrag(disk)
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

fun defrag(disk: MutableList<Int>): MutableList<Int> {
    val lastIndex = getLastFreeToMove(disk) ?: return disk
    for (i in 0 until lastIndex) {
        if (disk[i] == free) {
            disk[i] = disk[lastIndex]
            disk.removeAt(lastIndex)
            while (disk.lastOrNull() == free) {
                disk.removeAt(disk.size - 1)
            }
            return defrag(disk)
        }
    }
    throw IllegalStateException("Should not happen")
}

private fun getLastFreeToMove(disk: MutableList<Int>): Int? {
    if (!disk.contains(free)) {
        return null
    }
    for (j in disk.size - 1 downTo 0) {
        if (disk[j] != free) {
            return j
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
