package year2023.day2023_04

import puzzle

data class Copy(val cardNum: Int, val score: Int, var processed: Boolean = false)

fun main() {
    puzzle(30) { lines ->
        val copies = MutableList<MutableList<Copy>>(lines.size) { mutableListOf() }

        fun addCopies(
            cardNum: Int,
            score: Int
        ) {
               for (i in lines.indices) {
                   if (i > cardNum && i <= score + cardNum) {
                       copies[i].add(Copy(cardNum, score))
                   }
               }
           }

        fun processFirstUnprocessed(copies: List<MutableList<Copy>>): Boolean {
            val firstUnprocessed = copies.indexOfFirst { it.any { !it.processed } }
            if (firstUnprocessed == -1) {
                return false
            }
            val copy = copies[firstUnprocessed].first { !it.processed }
            copy.processed = true
            addCopies(copy.cardNum, copy.score)
            return true
        }

        lines.forEachIndexed { cardNum, game ->
            val (winning, have) = game.split(":")[1].trim().split("|")
            val winningNumbers = winning.trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }.toSet()
            val haveNumbers = have.trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
            val score = haveNumbers.count { it in winningNumbers }
            addCopies(cardNum, score)
        }

        while (processFirstUnprocessed(copies)) {
            // process all
        }
        copies.sumOf { it.count() + 1 }
    }
}
