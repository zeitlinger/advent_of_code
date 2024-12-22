package year2015.day2015_03

import Direction
import Point
import puzzle

fun main() {
    puzzle(2) { lines ->
        var pos = Point(0, 0)
        val visited = mutableSetOf(pos)
        lines[0].map { Direction.of(it) }.forEach { dir ->
            pos = pos.move(dir)
            visited += pos
        }
        visited.size
    }
}
