package year2023.day2023_06

import puzzle

data class RecordRace(val time: Int, val distance: Int)

fun bisectSmallest(range: LongRange, predicate: (Long) -> Boolean): Long {
    var left = range.first
    var right = range.last
    while (left < right) {
        val mid = left + (right - left) / 2
        if (predicate(mid)) {
            right = mid
        } else {
            left = mid + 1
        }
    }
    return left
}

fun bisectLargest(range: LongRange, predicate: (Long) -> Boolean): Long {
    var left = range.first
    var right = range.last
    while (left < right) {
        val mid = left + (right - left + 1) / 2
        if (predicate(mid)) {
            left = mid
        } else {
            right = mid - 1
        }
    }
    return left
}

fun waysToBeat(recordRace: RecordRace): Int {
    val predicate: (Long) -> Boolean = { chargeTime ->
       chargeTime * (recordRace.time - chargeTime) > recordRace.distance
    }
    val range = 1L until recordRace.time.toLong()
    val smallest = bisectSmallest(range, predicate)
    val largest = bisectLargest(range, predicate)
    return (largest - smallest + 1).toInt()
}

fun main() {
    puzzle(288) { lines ->
        val times = lines[0]
            .substringAfter(":")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
        val distances = lines[1]
            .substringAfter(":")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
        val records: List<RecordRace> = times.zip(distances).map { RecordRace(it.first, it.second) }

        records.map { waysToBeat(it) }.reduce { acc, i -> acc * i }
    }
}
