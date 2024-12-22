package year2015.day2015_14

import stringPuzzle

// Dancer can fly 37 km/s for 1 seconds, but then must rest for 36 seconds.
val regex = """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()

data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int, var score: Int = 0) {
    fun positionAt(seconds: Int): Int {
        val cycleTime = flyTime + restTime
        val cycles = seconds / cycleTime
        val remaining = seconds % cycleTime
        val fly = minOf(remaining, flyTime)
        return cycles * flyTime * speed + fly * speed

    }
}

fun main() {
    stringPuzzle("688") { lines ->
        val seconds = if (lines.test) 1000 else 2503
        lines.lines.map { line ->
            val (name, speed, flyTime, restTime) = regex.matchEntire(line)!!.destructured
            Reindeer(name, speed.toInt(), flyTime.toInt(), restTime.toInt())
        }.let { reindeers ->
            (1..seconds).map { second ->
                val winner = reindeers.maxByOrNull { it.positionAt(second) }!!
                winner.score++
            }
            reindeers.maxOf { it.score }.toString()
        }.toString()
    }
}
