package year2015.day2015_14

import puzzle
import stringPuzzle

// Dancer can fly 37 km/s for 1 seconds, but then must rest for 36 seconds.
val regex = """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()

data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int)

fun main() {
    stringPuzzle("1120") { lines ->
        val seconds = if (lines.test) 1000 else 2503
        lines.lines.map { line ->
            val (name, speed, flyTime, restTime) = regex.matchEntire(line)!!.destructured
            Reindeer(name, speed.toInt(), flyTime.toInt(), restTime.toInt())
        }.let { reindeers ->
            reindeers.map { reindeer ->
                val cycleTime = reindeer.flyTime + reindeer.restTime
                val cycles = seconds / cycleTime
                val remaining = seconds % cycleTime
                val flyTime = if (remaining > reindeer.flyTime) reindeer.flyTime else remaining
                cycles * reindeer.speed * reindeer.flyTime + flyTime * reindeer.speed
            }.maxOrNull()!!
        }.toString()
    }
}
