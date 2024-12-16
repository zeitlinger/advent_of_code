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
    puzzle(64) { lines ->
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

        val open =
//            TreeSet(compareBy<VisitWithScore> { it.score }
//                .thenBy { it.visit.point }
//                .thenBy { it.visit.direction })
        val visit = Visit(start, Direction.RIGHT)
        open.add(VisitWithScore(visit, 0, listOf(visit)))
        val game = Game(maze, minScore, open)

        runGame(game)
    }
}

private fun runGame(game: Game): Int {
    val finalScores = mutableSetOf<VisitWithScore>()
    while (game.open.isNotEmpty()) {
        val visitWithScore = game.open.removeFirst()
//        println("Trying visit: ${visitWithScore.visit.point} with score: ${visitWithScore.score} - other visits: ${game.open.map { "s=${it.score} p=${it.visit.point}" }}")
//        game.maze.print(visitWithScore.path)

        val finalScore = moveReindeer(visitWithScore, game)
        if (finalScore != null) {
            finalScores.add(finalScore)
            println("Final score: ${finalScore.score}")
            game.maze.print(finalScore.path)
        }
    }

    val score = finalScores.minOf { it.score }

    val bestPlaces = finalScores
        .filter { it.score == score }
        .flatMap { it.path.map { it.point } }.toSet()
    game.maze.print(bestPlaces.map { Visit(it, Direction.RIGHT) })
    return bestPlaces.size
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

    println("Trying visit: $visit with score: $score")
    maze.print(visitWithScore.path)

    if (visitWithScore.path.take(visitWithScore.path.size - 1).contains(visit)) {
//        return null
    }

    val minScore = game.minScore
    val last = minScore[visit]
    if (last != null && last < score) {
        println("Skipping visit: $visit with score: $score")
        return null
    }
    minScore[visit] = score

    val open = game.open
    val forward = start.move(direction)
    if (maze.tile(forward) != Tile.WALL) {
        open.add(VisitWithScore(Visit(forward, direction), score + 1, visitWithScore.path + Visit(forward, direction)))
    }       else {
        println("Wall at $forward")
    }

    fun addTurn(old: Direction, turn: (Direction) -> Direction) {
        val d = turn(old)
        val n = start.move(d)
//        if (maze.tile(n) != Tile.WALL
//            &&
//            !minScore.containsKey(Visit(n, direction)) &&
//            !minScore.containsKey(Visit(n, old.opposite()))
//        ) {
            open.add(
                VisitWithScore(
                    Visit(start, d),
                    score + 1000,
                    visitWithScore.path + Visit(start, d)
                )
            )
//        }
    }

    addTurn(direction, Direction::turnLeft)
    addTurn(direction, Direction::turnRight)
    return null
}
