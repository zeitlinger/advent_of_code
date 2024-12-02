package day02

import puzzle

fun main() {
    safe("69 67 70 72 73 74 76")
    puzzle(4) { reports ->
        reports.count { report ->
            safe(report)
        }
    }
}

private fun safe(report: String): Boolean {
    val levels = report.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val safe = checkLevels(levels, true) || checkLevels(levels.drop(1), false)
    println("$report: $safe")
    return safe
}

private fun checkLevels(levels: List<Int>, tryAgain: Boolean): Boolean {
    var backup: Int? = null
    var backupUsed = !tryAgain
    val range = if (levels[1] < levels[0]) {
        // decreasing order
        -3..-1
    } else {
        // increasing order
        1..3
    }

    fun check(list: List<Int>, index: Int): Boolean {
        val f = backup ?: list[0]
        backup = null
        val safe = list[1] - f in range
        return if (!safe && !backupUsed) {
            backupUsed = true
            backup = list[0]
            true
        } else {
            safe
        }
    }

    val safe = levels.windowed(2).mapIndexed { index, it ->
        check(it, index)
    }.all { it }
    return safe
}
