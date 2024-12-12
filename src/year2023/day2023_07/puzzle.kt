package year2023.day2023_07

import puzzle

enum class Card {
    A, K, Q, J, T, _9, _8, _7, _6, _5, _4, _3, _2;

    companion object {
        fun of(c: Char): Card = when (c) {
            'A' -> A
            'K' -> K
            'Q' -> Q
            'J' -> J
            'T' -> T
            '9' -> _9
            '8' -> _8
            '7' -> _7
            '6' -> _6
            '5' -> _5
            '4' -> _4
            '3' -> _3
            '2' -> _2
            else -> {
                throw IllegalArgumentException("Invalid card $c")
            }
        }
    }
}

data class Hand(val cards: List<Card>, val bid: Int)

fun compareHands(h1: Hand, h2: Hand): Int {
    val r1 = rank(h1)
    val r2 = rank(h2)
    if (r1 != r2) {
        return r1 - r2
    }
    h1.cards.zip(h2.cards).forEach { (c1, c2) ->
        if (c1 != c2) {
            return c2.ordinal - c1.ordinal
        }
    }
    return 0
}

fun rank(hand: Hand): Int {
    val g = hand.cards.groupBy { it }.values.sortedByDescending { it.size }
    if (g[0].size == 5) {
        // 5 of a kind
        return 7
    }
    if (g[0].size == 4) {
        // 4 of a kind
        return 6
    }
    if (g[0].size == 3 && g[1].size == 2) {
        // full house
        return 5
    }
    if (g[0].size == 3) {
        // 3 of a kind
        return 4
    }
    if (g[0].size == 2 && g[1].size == 2) {
        // 2 pairs
        return 3
    }
    if (g[0].size == 2) {
        // 1 pair
        return 2
    }
    return 1
}

fun main() {
    puzzle(6440) { lines ->
        val sorted = lines.map {
            val s = it.split(" ")
            val hand = s[0]
            Hand(hand.map { c -> Card.of(c) }, s[1].toInt())
        }.sortedWith(::compareHands)
        sorted.mapIndexed { index, hand -> hand.bid * (index + 1) }.sum()
    }
}
