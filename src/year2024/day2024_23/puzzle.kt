package year2024.day2024_23

import puzzle
import permutations

val regex = Regex("""(\w+)-(\w+)""")

data class Connection(val computers: List<String>) {
    fun other(computer: String): String {
        return computers.first { it != computer }
    }

    fun isConnection(computer: String): Boolean {
        return computers.contains(computer)
    }
}

fun main() {
    puzzle(7) { lines ->
        val all = lines.map {
            regex.matchEntire(it)!!.destructured
                .let { (a, b) -> Connection(listOf(a, b)) }
        }
        val flatMap = all.flatMap { it.computers.filter { it.startsWith("t") } }
            .flatMap { inLanParty(all, it) }
            .toSet()
        flatMap.size
    }
}

fun inLanParty(all: List<Connection>, start: String): Set<Set<String>> {
    val second = all.filter { it.isConnection(start) }.flatMap { it.computers }
    val zip = second.flatMap { a -> second.map { a to it } }
    val list = zip
        .filter { (a, b) ->
            a != b && a != start && b != start && connected(all, a, b)
        }
    val distinct = list.map { (a, b) -> setOf(a, b, start) }
        .toSet()
    return distinct
}

fun connected(all: List<Connection>, a: String, b: String): Boolean {
    return all.any { it.computers.contains(a) && it.computers.contains(b) }
}
