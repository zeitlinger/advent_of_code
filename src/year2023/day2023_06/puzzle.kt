package year2023.day2023_06

import bisectLargest
import bisectSmallest
import puzzle

data class RecordRace(val time: Long, val distance: Long)

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
