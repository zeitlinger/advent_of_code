package year2015.day2015_02

import puzzle

fun main() {
//    A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
    puzzle(34) { lines ->
        lines.sumOf {
            val dimensions = it.split("x").map { it.toInt() }
            val (l, w, h) = dimensions
            val wrap = 2 * dimensions.sorted().take(2).sum()
            val bow = l * w * h
            wrap + bow
        }
    }
}
