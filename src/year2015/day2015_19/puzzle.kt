package year2015.day2015_19

import puzzle

data class Replacement(val from: String, val to: String)

fun main() {
    puzzle(7) { lines ->
        val start = lines.last()
        val replacements = lines.dropLast(1).map {
            val (from, to) = it.split(" => ")
            Replacement(from, to)
        }
        applyReplacements(start, replacements)
    }
}

fun applyReplacements(start: String, replacements: List<Replacement>): Int {
    val results = mutableSetOf<String>()
    replacements.forEach { r ->
        val regex = Regex(r.from)
        regex.findAll(start).forEach { find ->
            val result = start.replaceRange(find.range, r.to)
            results.add(result)
        }
    }
    return results.size
}
