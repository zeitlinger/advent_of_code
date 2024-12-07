package year2024.day04

import puzzle

fun main() {
    puzzle(9) { lines ->
        var net = lines.map { it.map { "." }.toMutableList() }.toMutableList()
        fun find(lineNo: Int, colNo: Int): Boolean {
            fun isChar(want: Char, dir: Direction): Boolean {
                val nextLineNo = lineNo + dir.dy
                val nextColNo = colNo + dir.dx
                if (nextLineNo !in lines.indices || nextColNo !in lines[nextLineNo].indices) {
                    return false
                }
                return lines[nextLineNo][nextColNo] == want
            }

            if (lines[lineNo][colNo] != 'A') {
                return false
            }

            val b = ((isChar('M', Direction.NORTH_WEST) && isChar('S', Direction.SOUTH_EAST) ||
                    (isChar('S', Direction.NORTH_WEST) && isChar('M', Direction.SOUTH_EAST)))) &&
                    ((isChar('S', Direction.NORTH_EAST) && isChar('M', Direction.SOUTH_WEST) ||
                            (isChar('M', Direction.NORTH_EAST) && isChar('S', Direction.SOUTH_WEST))))
            if (b) {
                net[lineNo][colNo] = "X"
            }

            return b
        }

        var found = 0

        for (lineNo in lines.indices) {
            for (colNo in lines[lineNo].indices) {
                if (find(lineNo, colNo)) {
                    println("Found X-MAS at $lineNo, $colNo")
                    found++
                }
            }
        }
        println(net.joinToString("\n") { it.joinToString("") })
        found
    }
}

