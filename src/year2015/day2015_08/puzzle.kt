package year2015.day2015_08

import puzzle

fun main() {
    puzzle(19) { lines ->
        val memorySize = lines.sumOf {
            it.length
        }
        val stringSize = lines.sumOf { encode(it).length }
        stringSize - memorySize
    }
}

fun encode(s: String): String {
    return "\"${s.replace(Regex("""(\\|")""")) { "\\${it.value}" }}\""
}
