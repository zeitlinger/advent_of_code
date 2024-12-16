package year2024.day2024_16

import Direction
import Point
import puzzle
import java.util.TreeSet

enum class Tile(val symbol: Char) {
    WALL('#'),
    EMPTY('.'),
    START('S'),
    END('E');

    companion object {
        fun of(c: Char): Tile {
            return entries.single { it.symbol == c }
        }
    }
}

data class Maze(val tiles: List<List<Tile>>) {
    fun print(path: List<Visit>) {
        tiles.forEachIndexed { y, row ->
            row.forEachIndexed { x, tile ->
                val point = Point(x, y)
                val visit = path.find { it.point == point }
                if (visit != null) {
                    print(visit.direction.symbol)
                } else {
                    print(tile.symbol)
                }
            }
            println()
        }
    }

    fun tile(point: Point): Tile {
        return tiles[point.y][point.x]
    }
}

data class Visit(val point: Point, val direction: Direction)

data class VisitWithScore(val visit: Visit, val score: Int, val path: List<Visit>)

data class Game(val maze: Maze, val minScore: MutableMap<Visit, Int?>, val open: TreeSet<VisitWithScore>)

fun main() {
    puzzle(11048) { lines ->
        val maze = Maze(lines.map { it.map { c -> Tile.of(c) } })
        val minScore = mutableMapOf<Visit, Int?>()
        maze.print(emptyList())

        val start = maze.tiles.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, tile ->
                if (tile == Tile.START) Point(
                    x,
                    y
                ) else null
            }
        }.flatten().first()

        val open = TreeSet(compareBy<VisitWithScore> { it.score }.thenBy { it.visit.point }.thenBy { it.visit.direction })
        open.add(VisitWithScore(Visit(start, Direction.RIGHT), 0, emptyList()))
        val game = Game(maze, minScore, open)

        runGame(game)
    }
}

private fun runGame(game: Game): Int {
    val finalScores = mutableSetOf<Int>()
    while (game.open.isNotEmpty()) {
        val visitWithScore = game.open.removeFirst()
//        println("Trying visit: ${visitWithScore.visit.point} with score: ${visitWithScore.score} - other visits: ${game.open.map { "s=${it.score} p=${it.visit.point}" }}")
//        game.maze.print(visitWithScore.path)

        val finalScore = moveReindeer(visitWithScore, game)
        if (finalScore != null) {
            finalScores.add(finalScore.score)
            println("Final score: ${finalScore.score}")
            game.maze.print(finalScore.path)
        }
    }

    return finalScores.minOrNull() ?: throw IllegalStateException("No solution")
}

fun moveReindeer(visitWithScore: VisitWithScore, game: Game): VisitWithScore? {
    val score = visitWithScore.score
    val visit = visitWithScore.visit
    val direction = visit.direction
    val start = visit.point
    val maze = game.maze
    if (maze.tile(start) == Tile.END) {
        return visitWithScore
    }

    val minScore = game.minScore
    val last = minScore[visit]
    if (last != null && last <= score) {
        return null
    }
    minScore[visit] = score

//    minScore[visit] = null // don't visit again

    val open = game.open
    val forward = start.move(direction)
    if (maze.tile(forward) != Tile.WALL) {
        open.add(VisitWithScore(Visit(forward, direction), score + 1, visitWithScore.path + Visit(forward, direction)))
    }

    fun addTurn(old: Direction, turn: (Direction) -> Direction) {
        val d = turn(old)
        val n = start.move(d)
        if (maze.tile(n) != Tile.WALL
//            &&
//            !minScore.containsKey(Visit(n, direction)) &&
//            !minScore.containsKey(Visit(n, old.opposite())
//            )
        ) {
            open.add(
                VisitWithScore(
                    Visit(start, d),
                    score + 1000,
                    visitWithScore.path + Visit(start, d)
                )
            )
        }
    }

    addTurn(direction, Direction::turnLeft)
    addTurn(direction, Direction::turnRight)
    return null
}
