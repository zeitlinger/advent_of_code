package year2023.day2023_04

import puzzle

fun main() {
    puzzle(30) { lines ->
        val copyCount = MutableList(lines.size) { 1 }

        lines.forEachIndexed { cardNum, game ->
            val (winning, have) = game.split(":")[1].trim().split("|")
            val winningNumbers = winning.trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }.toSet()
            val haveNumbers = have.trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
            val score = haveNumbers.count { it in winningNumbers }
            val copies = copyCount[cardNum]
            for (i in lines.indices) {
                if (i > cardNum && i <= score + cardNum) {
                    copyCount[i] += copies
                }
            }
        }
        copyCount.sum()
    }
}
