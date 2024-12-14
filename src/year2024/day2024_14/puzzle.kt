package year2024.day2024_14

import puzzle

data class Point(var x: Int, var y: Int) {

}

data class Robot(val location: Point, val velocity: Point) {

}

fun main() {
    puzzle(12) { lines ->
        val robots = lines.map { line ->
            val parts = line.split(" ")
            val location = toPoint(parts[0])
            val velocity = toPoint(parts[1])
            Robot(location, velocity)
        }
        val minX = robots.minOf { it.location.x }
        val maxX = robots.maxOf { it.location.x }
        val minY = robots.minOf { it.location.y }
        val maxY = robots.maxOf { it.location.y }
        val width = maxX - minX + 1
        val height = maxY - minY + 1
        for(i in 0 until 100) {
            robots.forEach { robot ->
                robot.location.x = (robot.location.x + robot.velocity.x).mod(width)
                robot.location.y = (robot.location.y + robot.velocity.y).mod(height)
            }
        }
        printAll(robots)
        safetyFactor(robots)
    }
}

fun safetyFactor(robots: List<Robot>): Int {
    val minX = robots.minOf { it.location.x }
    val maxX = robots.maxOf { it.location.x }
    val minY = robots.minOf { it.location.y }
    val maxY = robots.maxOf { it.location.y }
    val width = maxX - minX + 1
    val height = maxY - minY + 1
    val q1 = robots.count { it.location.x < width / 2 && it.location.y < height / 2 }
    val q2 = robots.count { it.location.x > width / 2 && it.location.y < height / 2 }
    val q3 = robots.count { it.location.x < width / 2 && it.location.y > height / 2 }
    val q4 = robots.count { it.location.x > width / 2 && it.location.y > height / 2 }
    return q1 * q2 * q3 * q4
}

fun printAll(robots: List<Robot>) {
    val minX = robots.minOf { it.location.x }
    val maxX = robots.maxOf { it.location.x }
    val minY = robots.minOf { it.location.y }
    val maxY = robots.maxOf { it.location.y }
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val count = robots.count { it.location.x == x && it.location.y == y }
            if (count > 0) {
                print(count)
            } else {
                print(".")
            }
        }
        println()
    }
}

private fun toPoint(s: String): Point = s.substringAfter("=").split(",")
    .let { Point(it[0].toInt(), it[1].toInt()) }
