package year2023.day2023_05

import puzzle

data class Mapping(val SrcFrom: Int, val dstFrom: Int, val length: Int)

fun main() {
    puzzle(35) { lines ->
        val seeds = lines[0].substringAfter(":").trim().split(" ").map { it.toInt() }
        1
    }
}
