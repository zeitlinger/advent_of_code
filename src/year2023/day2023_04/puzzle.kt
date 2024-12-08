package year2023.day2023_04

import puzzle
import kotlin.math.pow

fun main() {
    puzzle(13) { lines ->
        lines.sumOf { game ->
            val (winning, have) = game.split(":")[1].trim().split("|")
            val winningNumbers = winning.trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }.toSet()
            val haveNumbers = have.trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
            val score = haveNumbers.count { it in winningNumbers }
            if (score == 0) 0 else 2.0.pow(score).toInt() / 2
        }
    }
}
