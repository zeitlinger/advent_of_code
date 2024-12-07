package year2024.day06

import puzzle

fun main() {
    puzzle(41) { lines ->
        var pos = getStart(lines)
        var direction = Direction.NORTH
        val maze = lines.map { it.replace('^', '.').toCharArray().toMutableList() }.toMutableList()

        fun isWall(p: Point): Boolean {
            return maze[p.y][p.x] == '#'
        }

        fun isInside(p: Point): Boolean {
            return p.y in maze.indices && p.x in maze[p.y].indices
        }

        fun mark(p: Point) {
            maze[p.y][p.x] = 'X'
        }

        fun countVisited(): Int {
            return maze.sumOf { it.count { it == 'X' } }
        }

        fun print() {
            println("Position: $pos")
            println(maze.joinToString("\n") { it.joinToString("") })
        }

        while (true) {
            mark(pos)
            val next = direction.ahead(pos)
            if (!isInside(next)) {
                break
            }

            if (isWall(next)) {
                direction = direction.turnRight()
            } else {
                pos = next
            }
        }

        print()

        countVisited()
    }
}

private fun getStart(lines: List<String>): Point {
    for (row in lines.indices) {
        for (col in lines[row].indices) {
            if (lines[row][col] == '^') {
                return Point(col, row)
            }
        }
    }
    throw IllegalArgumentException("No start found")
}
