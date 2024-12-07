package year2024.day04

import puzzle

enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1)
}

fun main() {
    puzzle(18) { lines ->
        var net = lines.map { it.map { "." }.toMutableList() }.toMutableList()
        fun find(word: String, direction: Direction, lineNo: Int, colNo: Int): Boolean {
            if (word.isEmpty()) {
                return true
            }
            if (lineNo !in lines.indices || colNo !in lines[lineNo].indices) {
                return false
            }
            val want = word.first()
            if (want == lines[lineNo][colNo]) {
                val nextLineNo = lineNo + direction.dy
                val nextColNo = colNo + direction.dx
                val find = find(word.drop(1), direction, nextLineNo, nextColNo)
                if (find) {
                    net[lineNo][colNo] = want.toString()
                }
                return find
            }
            return false
        }

        var found = 0

        for (lineNo in lines.indices) {
            for (colNo in lines[lineNo].indices) {
                for (direction in Direction.entries) {
                    if (find("XMAS", direction, lineNo, colNo)) {
                        println("Found XMAS at $lineNo, $colNo, $direction")
                        found++
                    }
                }
            }
        }
        println(net.joinToString("\n") { it.joinToString("") })
        found
    }
}

