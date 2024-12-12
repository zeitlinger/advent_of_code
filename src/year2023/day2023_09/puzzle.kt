package year2023.day2023_09

import puzzle

data class Sequence(val values: List<Long>)

fun main() {
    puzzle(114) { lines ->
        lines.sumOf {
            val sequence = Sequence(it.split(" ").map { it.toLong() })
            next(sequence)
        }
    }
}

fun next(sequence: Sequence): Long {
    var values = sequence.values
    val slopes = mutableListOf(values)
    while (values.any { it != 0L }) {
        val list = slope(values)
        slopes.add(list)
        values = list
    }
    return slopes.sumOf { it.last() }
}

fun slope(values: List<Long>): List<Long> {
    return values.drop(1).mapIndexed { index, l ->
        l - values[index]
    }
}
