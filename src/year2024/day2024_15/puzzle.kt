package year2024.day2024_15

import puzzle

data class Point(val x: Int, val y: Int)

enum class Direction(val x: Int, val y: Int, symbol:    ) {
    UP(0, -1, "^"),
    DOWN(0, 1, "v"),
    LEFT(-1, 0, "<"),
    RIGHT(1, 0, ">")

    fun move(p: Point): Point {
        return Point(p.x + x, p.y + y)
    }
}

en

fun main() {
    puzzle(10092) { lines ->
        0
    }
}
