package year2024.day2024_22

import puzzle

const val modulo = 2.shl(23).toLong()

data class Random(var value: Long) {
    fun next(): Long {
        generate(value.shl(6))
        generate(value.shr(5))
        generate(value.shl(11))
        return value
    }

    private fun generate(factor: Long) {
        val xor = value.xor(factor)
        val mod = xor.mod(modulo)
//        println("value: $value, factor: $factor xor: $xor mod: $mod")
        value = mod
    }
}

fun main() {
    puzzle(23) { lines ->
//        val random = Random(123)
//        var price = 0
//        for (i in 0 until 20) {
//            price = random.next().mod(10)
//            println(price)
//        }

        val prices = lines.map {
            prices(Random(it.toLong()))
        }
        val sequences = prices.flatMap { it.windowed(5).map { delta(it) } }.distinct()
        println(sequences.size)
        sequences.maxOfOrNull { bananaCount(it, prices) }!!
    }
}

fun bananaCount(lookFor: List<Long>, prices: List<List<Long>>): Long {
    val count = prices.mapIndexed { index, price ->
        val i = price.windowed(5).indexOfFirst { delta(it) == lookFor }
//        val s = lookFor.joinToString(",")
//        if (s == "-2,1,-1,3" || s == "7,-6,5,2") {
//            println("i: $i seq: $lookFor index: $index")
//            if (i > -1) {
//                println(price.slice(i until i + 5))
//            }
//        }
        if (i == -1) { 0 } else price[i + 4]
    }.sum()
//    if (count == 27L) {
//        println("seq: $lookFor count: $count")
//    }
//    println("seq: $lookFor count: $count")
    return count
}

fun delta(prices: List<Long>): List<Long> {
    return prices.mapIndexed { index, l ->
        if (index == 0) {
            0
        } else {
            l - prices[index - 1]
        }
    }.drop(1)
}

fun prices(random: Random): List<Long> {
    val map = (0 until 2000).map {
        val mod: Long = random.next().mod(10L)
        mod
    }
    return map
}
