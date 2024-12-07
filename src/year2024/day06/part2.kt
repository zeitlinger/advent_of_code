package year2024.day06

import puzzle

data class Point(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int, val symbol: Char) {
    NORTH(0, -1, '^'), EAST(1, 0, '>'), SOUTH(0, 1, 'v'), WEST(-1, 0, '<');

    fun turnRight(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun ahead(p: Point): Point = Point(p.x + dx, p.y + dy)
}

data class Visit(val pos: Point, val direction: Direction)

fun main() {
    puzzle(6) { lines ->
        val allPoints = lines.flatMapIndexed { y, row ->
            List(row.length) { x ->
                Point(x, y)
            }
        }
        val start = allPoints.first { lines[it.y][it.x] == '^' }

        fun simulateObstruction(obstruction: Point): Boolean {
            val maze = lines.map { it.replace('^', '.').toCharArray().toMutableList() }.toMutableList()
            var steps = 0

            fun isInside(p: Point): Boolean {
                return p.y in maze.indices && p.x in maze[p.y].indices
            }

            fun isWall(p: Point): Boolean {
                return maze[p.y][p.x] == '#' || p == obstruction
            }

            val visited = mutableSetOf<Visit>()
            var pos = start
            var direction = Direction.NORTH

            fun print() {
                if (steps < 100000 || steps % 1000 != 0) {
                    return
                }
                maze.toMutableList().apply {
                    this[pos.y][pos.x] = direction.symbol
                    this[obstruction.y][obstruction.x] = 'O'
                }.also {
                    println("Steps: $steps")
                    println(it.joinToString("\n") { it.joinToString("") })
                }
            }

            fun visitHasFoundLoop(p: Point): Boolean {
                maze[p.y][p.x] = 'X'
                return !visited.add(Visit(p, direction))
            }

            while (true) {
                print()
                if (visitHasFoundLoop(pos)) {
                    return true
                }
                val next = direction.ahead(pos)
                if (!isInside(next)) {
                    return false
                }

                if (isWall(next)) {
                    direction = direction.turnRight()
                } else {
                    pos = next
                    steps++
                }
            }
        }

        allPoints.filter { it != start }.count { simulateObstruction(it) }
    }
}
