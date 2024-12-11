package year2024.day2024_11

import puzzle

fun main() {
    puzzle(55312) { lines ->
        var stones = lines[0].split(" ").map { it.toLong() }.toMutableList()
        for (blink in 1..25) {
            val newStones = mutableListOf<Long>()
            for (value in stones) {
                val s = value.toString()
                when {
                    value == 0L -> newStones.add(1)
                    s.length.mod(2) == 0 -> {
                        newStones.add(s.substring(0, s.length / 2).toLong())
                        newStones.add(s.substring(s.length / 2).toLong())
                    }
                    else -> {
                        newStones.add(value * 2024)
                    }
                }
            }
            stones = newStones
//            println(stones)
        }
        stones.size
    }
}
