package year2024.day2024_25

import puzzle
import stringPuzzle

data class Lock(val pinHeights: List<Int>)

data class Key(val heights: List<Int>)

fun main() {
    stringPuzzle("3") { input ->
        val locks = input.lineBlocks()
            .filter { it[0][0] == '#' }
            .map { block ->
                val pinHeights = MutableList(block[0].length) { 0 }
                block.drop(1).forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '#') {
                            pinHeights[index]++
                        }
                    }
                }
                Lock(pinHeights)
            }
        val keys = input.lineBlocks()
            .filter { it[0][0] == '.' }
            .map { block ->
                val heights = MutableList(block[0].length) { 0 }
                block.reversed().drop(1).forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '#') {
                            heights[index]++
                        }
                    }
                }
                Key(heights)
            }
        locks.sumOf { lock ->
            keys.count { key -> fits(key, lock) }
        }.toString()
    }
}

fun fits(key: Key, lock: Lock): Boolean = key.heights.zip(lock.pinHeights).none { (k, l) -> k + l > 5 }
