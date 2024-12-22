package year2015.day2015_03

import Direction
import Point
import puzzle

fun main() {
    puzzle(11) { lines ->
        var santa = Point(0, 0)
        var robo = Point(0, 0)
        val visited = mutableSetOf(santa)
        lines[0].map { Direction.of(it) }
            .chunked(2)
            .forEach { dir ->
                santa = santa.move(dir[0])
                robo = robo.move(dir[1])
                visited += santa
                visited += robo
            }
        visited.size
    }
}
