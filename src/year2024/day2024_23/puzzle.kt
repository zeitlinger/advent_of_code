package year2024.day2024_23

import puzzle

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
        all.flatMap { it.computers.filter { it.startsWith("t") } }
            .sumOf { inLanParty(all, it) }
    }
}

fun inLanParty(all: List<Connection>, start: String): Int {
    val second = all.filter { it.isConnection(start) }.map { it.other(start) }.toSet()
    val c3 = all
        .filter { s -> s.computers.any { it in second }
                && s.computers.none { it == start } }.flatMap { it.computers }

    val sets = c3.map { c3 ->
        val c2 = all
            .filter { it.computers.none { it == start } && it.computers.any { it in second } }
            .first { it.isConnection(c3) }.other(c3)
        setOf(
            start, c3, c2
        )
    }
    return sets.size
}

fun connected(all: List<Connection>, a: String, b: String): Boolean {
    return all.any { it.computers.contains(a) && it.computers.contains(b) }
}
