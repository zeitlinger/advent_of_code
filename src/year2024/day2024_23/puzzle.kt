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
    val second = all.filter { it.isConnection(start) }
    val list = second.flatMap { it.computers }
        .filter { it != start }
        .map { c2 ->
//            val avoid = setOf(start, it)
            val c3 = all
                .filter { c3 ->
                    c3.isConnection(c2) && c3.computers.any { it == start }
                }
            c3.map {
                val set = mutableSetOf<String>()
                set.addAll(it.computers)
                set.add(start)
                set
            }
        }
    return list
        .count()
}

fun connected(all: List<Connection>, a: String, b: String): Boolean {
    return all.any { it.computers.contains(a) && it.computers.contains(b) }
}
