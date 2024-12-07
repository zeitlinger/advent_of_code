package day02

import puzzle

fun main() {
    puzzle(2) { reports ->
        reports.count { report ->
            val levels = report.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val first = levels[0]
            val second = levels[1]
            val safe = if (second < first) {
                // decreasing order
                levels.windowed(2).all {
                    it[1] - it[0] in -3..-1
                }
            } else {
                // increasing order
                levels.windowed(2).all {
                    it[1] - it[0] in 1..3
                }
            }
            println("$report: $safe")
            safe
        }
    }
}
