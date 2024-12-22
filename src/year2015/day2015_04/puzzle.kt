package year2015.day2015_04

import md5
import puzzle

fun main() {
    puzzle(609043) { lines ->
        val secret = lines[0]
        findMd5(secret)
    }
}

fun findMd5(secret: String): Int {
    var i = 0
    while (true) {
        val md5 = "$secret$i".md5()
        if (md5.startsWith("00000")) {
            return i
        }
        i++
    }
}
