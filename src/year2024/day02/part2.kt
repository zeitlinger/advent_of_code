package year2024.day02

import puzzle

fun main() {
    puzzle(4) { reports ->
        reports.count { report ->
            safe(report)
        }
    }
}

private fun safe(report: String): Boolean {
    val levels = report.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val safe =
        levels.filterIndexed { index, i ->
            val list = levels.toMutableList()
            list.removeAt(index)
            checkLevels(list)
        }.any()
    println("$report: $safe")
    return safe
}

private fun checkLevels(levels: List<Int>): Boolean {
    val range = if (levels[1] < levels[0]) {
        // decreasing order
        -3..-1
    } else {
        // increasing order
        1..3
    }

    return levels.windowed(2).mapIndexed { index, it ->
        it[1] - it[0] in range
    }.all { it }
}
