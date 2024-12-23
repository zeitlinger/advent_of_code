package year2024.day2024_23

import stringPuzzle

val regex = Regex("""(\w+)-(\w+)""")

data class Connection(val computers: Set<String>) {
    fun other(computer: String): String {
        return computers.first { it != computer }
    }

    fun isConnection(computer: String): Boolean {
        return computers.contains(computer)
    }
}

fun main() {
    stringPuzzle("co,de,ka,ta") { lines ->
        val all = lines.lines.map {
            regex.matchEntire(it)!!.destructured.let { (a, b) -> Connection(setOf(a, b)) }
        }
        var largestParty = setOf<String>()
        all.forEach { connection ->
            if (connection.computers.first() in largestParty) {
                return@forEach
            }
            val party = largestParty(all, connection.computers)
            if (party.size > largestParty.size) {
                largestParty = party

            }
        }
        largestParty.sorted().joinToString(",")
    }
}

fun largestParty(all: List<Connection>, members: Set<String>): Set<String> {
    val tryNext = all.filter { next ->
        next.computers.any { it in members } && next.computers.any { it !in members }
    }.flatMap { it.computers }.toSet() - members
    val next = tryNext.firstOrNull { new -> members.all { connected(all, it, new) } } ?: return members
    return largestParty(all, members + next)
}

fun connected(all: List<Connection>, a: String, b: String): Boolean {
    return all.any { it.computers.contains(a) && it.computers.contains(b) }
}
