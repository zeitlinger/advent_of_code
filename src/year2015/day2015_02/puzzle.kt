package year2015.day2015_02

import puzzle

fun main() {
//    A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping paper plus 6 square feet of slack, for a total of 58 square feet.
    puzzle(58) { lines ->
        lines.sumOf {
            val (l, w, h) = it.split("x").map { it.toInt() }
            val sides = listOf(l * w, w * h, h * l)
            val slack = sides.minOrNull()!!
            2 * sides.sum() + slack
        }
    }
}
