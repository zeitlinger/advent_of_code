package year2024.day2024_13

import puzzle

data class Point(val x: Long, val y: Long) {
    fun plus(point: Point): Point {
        return Point(x + point.x, y + point.y)
    }

    companion object {
        fun of(s: String): Point {
            val x = s.substringAfter("X")
                .drop(1)
                .substringBefore(",")
            val y = s
                .substringAfter("Y")
                .drop(1)
            return Point(x.toLong(), y.toLong())
        }
    }
}

data class Button(val p: Point, val cost: Long) {
    fun slope(): Double {
        return p.y.toDouble() / p.x
    }
}

data class ClawConfig(val price: Point, val buttonA: Button, val buttonB: Button)

fun fewestTokensNeededToWin(clawConfig: ClawConfig): Long? {
    val price = clawConfig.price
    val buttonA = clawConfig.buttonA
    val buttonB = clawConfig.buttonB
    val maxSlopeButton = if (buttonA.slope() > buttonB.slope()) buttonA else buttonB
    val minSlopeButton = if (buttonA.slope() < buttonB.slope()) buttonA else buttonB
    println("Max slope: ${maxSlopeButton.slope()}, min slope: ${minSlopeButton.slope()}")
    var x = 0L
    var y = 0L
    var tokens = 0L
    var tokensPerTry = 1L
    val maxPerToken = listOf(buttonA.p.x, buttonA.p.y, buttonB.p.x, buttonB.p.y).maxOrNull()!!

    fun move(button: Button) {
        x += button.p.x * tokensPerTry
        y += button.p.y * tokensPerTry
        tokens += button.cost * tokensPerTry
        println("Moving $tokensPerTry tokens with slope ${button.slope()} now at $x, $y")
    }

    while (x < price.x && y < price.y) {
        val distanceX = price.x - x
        val distanceY = price.y - y
        val minDistance = minOf(distanceX, distanceY)
        val maxMove = maxPerToken * tokensPerTry
        println("Distance: ${distanceX.toDouble()}, ${distanceY.toDouble()} maxMove: ${maxMove.toDouble()}")
        val scale = 2
        if (minDistance > scale * maxMove) {
            tokensPerTry *= scale
            println("Upscaling to $tokensPerTry")
        } else {
            var downScale = 100
            val b = distanceX > maxMove / scale
            while (tokensPerTry > 1 && downScale > 1) {
                downScale--
                tokensPerTry /= scale
                println("Downscaling to $tokensPerTry")
            }
        }

        val targetSlope = distanceY.toDouble() / distanceX
        val r1 = maxSlopeButton.slope() / targetSlope
        val r2 = targetSlope / minSlopeButton.slope()
        // 3 / 1 = 3
        // 1 / .3 = 3.333
        println("targetSlope: $targetSlope, r1: $r1, r2: $r2")
        if (r1 < r2) {
            move(maxSlopeButton)
        } else {
            move(minSlopeButton)
        }
        if (x == price.x && y == price.y) {
            println("For $clawConfig, possible tokens: $tokens")
            return tokens
        }
    }
    println("For $clawConfig, impossible")
    return null
}

fun main() {
    puzzle(null) { lines ->
        val delta = 10_000_000_000_000
        val clawConfigs = lines.chunked(3).map {
            ClawConfig(
                Point.of(it[2]).plus(Point(delta, delta)),
                Button(Point.of(it[0]), 3L),
                Button(Point.of(it[1]), 1L)
            )
        }
            .filterIndexed { index, clawConfig -> index == 1 } // for debugging
        println(clawConfigs)
        val list = clawConfigs.map { fewestTokensNeededToWin(it) }
        for (i in list.indices) {
            println("For $clawConfigs[i], $list[i]")
        }
        throw IllegalStateException("Done")
        list.filterNotNull().sum()
    }
}
