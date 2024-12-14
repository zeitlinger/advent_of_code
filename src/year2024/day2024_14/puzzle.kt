package year2024.day2024_14

import puzzle

data class Point(var x: Int, var y: Int)
data class Robot(val location: Point, val velocity: Point)
data class MinTree(val width: Int, val height: Int)

fun main() {
    puzzle(null) { lines ->
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
        var seconds = 0
        val lastLine = lastLineLength(robots)
        while (true) {
            if (seconds % 100000 == 0) {
                println("Seconds: $seconds")
            }

            robots.forEach { robot ->
                robot.location.x = (robot.location.x + robot.velocity.x).mod(width)
                robot.location.y = (robot.location.y + robot.velocity.y).mod(height)
            }
            seconds++
            if (isXmasTree(robots, lastLine)) {
                printAll(robots)
                return@puzzle seconds
            }
        }
        0
    }
}

fun lastLineLength(robots: List<Robot>): MinTree {
    val want = robots.size / 2 + 1
    var lineNumber = 1
    var lineLength = 1
    var sum = 1
    while (sum < want) {
        lineNumber++
        lineLength += 2
        sum += lineLength
    }
    return MinTree(lineLength, lineNumber)
}

fun isXmasTree(robots: List<Robot>, minTree: MinTree): Boolean {
    val groups = robots.groupBy { it.location.y }.toSortedMap()
    val width = minTree.width
    groups
        .filter { it.key >= minTree.height - 1 }
        .forEach { (y, group) ->
            if (group.size >= width) {
                return findTree(group, width, groups, robots)
            }
        }
    return false
}

private fun findTree(
    group: List<Robot>,
    width: Int,
    groups: Map<Int, List<Robot>>,
    robots: List<Robot>
): Boolean {

    val sorted = group.sortedBy { it.location.x }
    val i = findConsecutive(sorted, width)
    if (i < 0) {
        return false
    }
    println("Find next line $width")
    printAll(robots)
    val p = sorted[i].location
    // I ended up solving this by setting a breakpoint here and observing the data
    val line = width - 2
    groups[Point(p.x + 1, p.y - 1).y]?.let { next ->
        if (next.size >= line) {
            return findTree(next, line, groups, robots)
        }
    }
    return false
}

fun findConsecutive(sorted: List<Robot>, want: Int): Int {
    val firstX = sorted.first().location.x
    for (i in 1 until sorted.size) {
        val x = sorted[i].location.x
        if (firstX + i != x) {
            if (sorted.size - i < want) {
                return -1
            }
            return findConsecutive(sorted.drop(i), want)
        }
        if (i == want - 1) {
            return i
        }
    }
    return -1
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
