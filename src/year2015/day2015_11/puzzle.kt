package year2015.day2015_11

import puzzle
import stringPuzzle

fun main() {
    stringPuzzle("abcdffaa") { lines ->
        var password = lines.lines[0]
        while (!valid(password)) {
            password = increment(password)
        }
        password
    }
}

fun increment(password: String): String {
    val chars = password.toCharArray()
    for (i in chars.size - 1 downTo 0) {
        if (chars[i] == 'z') {
            chars[i] = 'a'
        } else {
            chars[i] = chars[i] + 1
            break
        }
    }
    return String(chars)
}

fun valid(password: String): Boolean {
    if (password.contains("i") || password.contains("o") || password.contains("l")) {
        return false
    }
    if (!password.contains(Regex("""(.)\1.*(.)\2"""))) {
        return false
    }
    if (!password.contains(Regex("""abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz"""))) {
        return false
    }
    return true
}
