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

data class Prices(val values: List<Int>, val deltas: List<List<Int>>)

fun main() {
    puzzle(23) { lines ->
//        val random = Random(123)
//        var price = 0
//        for (i in 0 until 20) {
//            price = random.next().mod(10)
//            println(price)
//        }

        val prices = lines.map {
            val values = prices(Random(it.toLong()))
            Prices(values, values.windowed(5).mapIndexed { index, ints ->
                val delta = delta(ints)
                delta
            })
        }
        val sequences = prices.flatMap { it.deltas }.distinct()
        println(sequences.size)
        sequences.maxOfOrNull { bananaCount(it, prices) }!!
    }
}

fun bananaCount(lookFor: List<Int>, prices: List<Prices>): Int {
    val count = prices.mapIndexed { index, price ->
        val i = price.deltas.indexOfFirst { it == lookFor }
//        val s = lookFor.joinToString(",")
//        if (s == "-2,1,-1,3" || s == "7,-6,5,2") {
//            println("i: $i seq: $lookFor index: $index")
//            if (i > -1) {
//                println(price.slice(i until i + 5))
//            }
//        }
        if (i == -1) {
            0
        } else price.values[i + 4]
    }.sum()
//    if (count == 27L) {
//        println("seq: $lookFor count: $count")
//    }
//    println("seq: $lookFor count: $count")
    return count
}

fun delta(prices: List<Int>): List<Int> {
    return prices.mapIndexed { index, l ->
        if (index == 0) {
            0
        } else {
            l - prices[index - 1]
        }
    }.drop(1)
}


fun prices(random: Random): List<Int> {
    val map = (0 until 2000).map {
        random.next().toInt().mod(10)
    }
    return map
}
