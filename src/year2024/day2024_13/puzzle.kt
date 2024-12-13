package year2024.day2024_13

import puzzle

data class Point(val x: Int, val y: Int) {
    companion object {
        fun of(s: String): Point {
            val x = s.substringAfter("X")
                .drop(1)
                .substringBefore(",")
            val y = s
                .substringAfter("Y")
                .drop(1)
            return Point(x.toInt(), y.toInt())
        }
    }
}

data class ClawConfig(val price: Point, val buttonA: Point, val buttonB: Point)

fun fewestTokensNeededToWin(clawConfig: ClawConfig): Int? {
    // no more than 100 pushes on each button
    // button A = 3 tokens
    // button B = 1 tokens
    val possibleTokens = mutableSetOf<Int>()
    for (a in 0..100) {
        for (b in 0..100) {
            val tokens = a * 3 + b
            val x = clawConfig.price.x - clawConfig.buttonA.x * a - clawConfig.buttonB.x * b
            val y = clawConfig.price.y - clawConfig.buttonA.y * a - clawConfig.buttonB.y * b
            if (x == 0 && y == 0) {
                possibleTokens.add(tokens)
            }
        }
    }
    return possibleTokens.minOrNull()
}

fun main() {
    puzzle(480) { lines ->
        val clawConfigs = lines.chunked(3).map {
            ClawConfig(
                Point.of(it[2]),
                Point.of(it[0]),
                Point.of(it[1])
            )
        }
        println(clawConfigs)
        clawConfigs.mapNotNull { fewestTokensNeededToWin(it) }.sum()
    }
}
