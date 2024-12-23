package year2015.day2015_18

import puzzle
import stringPuzzle

fun main() {
    stringPuzzle("4") { input ->
        val steps = if (input.test) 4 else 100
        var grid = input.lines.map { line ->
            line.map { it == '#' }
        }
        for (step in 1..steps) {
            grid = grid.mapIndexed { y, row ->
                row.mapIndexed { x, light ->
                    val neighbors = (-1..1).flatMap { dy ->
                        (-1..1).map { dx ->
                            val nx = x + dx
                            val ny = y + dy
                            if (nx in row.indices && ny in grid.indices && (nx != x || ny != y)) {
                                grid[ny][nx]
                            } else {
                                false
                            }
                        }
                    }
                    val count = neighbors.count { it }
                    if (light) {
                        count == 2 || count == 3
                    } else {
                        count == 3
                    }
                }
            }
        }
        grid.sumOf { row -> row.count { it } }.toString()
    }
}
