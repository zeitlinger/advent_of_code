package year2015.day2015_10

import puzzle
import stringPuzzle

fun main() {
    stringPuzzle("6") { lines ->
        var s = if (lines.test) "1" else "3113322113"
        val iterations = if (lines.test) 5 else 50
        repeat(iterations) {
            s = lookAndSay(s)
            println("Iteration ${it + 1}: ${s.length}")
        }
        s.length.toString()
    }
}

fun lookAndSay(s: String): String {
    val sb = StringBuilder()
    var i = 0
    while (i < s.length) {
        var count = 1
        while (i + 1 < s.length && s[i] == s[i + 1]) {
            i++
            count++
        }
        sb.append(count)
        sb.append(s[i])
        i++
    }
    return sb.toString()
}
