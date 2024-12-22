package year2015.day2015_08

import puzzle

fun main() {
    puzzle(12) { lines ->
        val stringSize = lines.sumOf { it.length }
        val memorySize = lines.sumOf {
            memorySize(it)
        }
        stringSize - memorySize
    }
}

val regex = Regex("""(\\x[0-9a-f]{2}|\\"|\\\\)""")

private fun memorySize(s: String): Int = when {
    s.startsWith("\"") -> memorySize(s.substring(1, s.length - 1))
    else -> {
        var result = s.length
        regex.findAll(s).forEach {
            result -= when  {
                it.value.startsWith("\\x") -> 3
                else -> 1
            }
        }
        result
    }

}
