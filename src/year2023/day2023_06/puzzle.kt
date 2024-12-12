package year2023.day2023_06

import puzzle

data class RecordRace(val time: Long, val distance: Long)

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

fun waysToBeat(recordRace: RecordRace): Long {
    val predicate: (Long) -> Boolean = { chargeTime ->
       chargeTime * (recordRace.time - chargeTime) > recordRace.distance
    }
    val range = 1L until recordRace.time
    val smallest = bisectSmallest(range, predicate)
    val largest = bisectLargest(range, predicate)
    return (largest - smallest + 1)
}

fun main() {
    puzzle(71503) { lines ->
        val time = lines[0]
            .substringAfter(":")
            .replace(" ", "")
            .toLong()
        val distance = lines[1]
            .substringAfter(":")
            .replace(" ", "")
            .toLong()
        val records: List<RecordRace> = listOf(RecordRace(time, distance))

        records.map { waysToBeat(it) }.reduce { acc, i -> acc * i }
    }
}
