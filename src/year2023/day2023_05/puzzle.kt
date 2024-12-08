package year2023.day2023_05

import puzzle

data class Mapping(val srcFrom: Long, val dstFrom: Long, val length: Long)

data class Rule(val src: String, val dst: String, val mappings: MutableList<Mapping>)

data class Product(val type: String, val ids: List<Long>)

fun main() {
    puzzle(35) { lines ->
        val headerRegex = "(.+)-to-(.+) map:".toRegex()
        val seeds = lines[0].substringAfter(":").trim().split(" ").map { it.toLong() }
        val rules = mutableListOf<Rule>()
        lines.drop(1).forEach { line ->
            val header = headerRegex.matchEntire(line)
            if (header != null) {
                val (src, dst) = header.destructured
                rules.add(Rule(src, dst, mutableListOf()))
            } else {
                val (dstFrom, srcFrom, length) = line.split(" ").map { it.toLong() }
                rules.last().mappings.add(Mapping(srcFrom, dstFrom, length))
            }
        }
        val ruleMap = rules.associateBy { it.src }

        fun translate(input: Product): Product {
            val rule = ruleMap[input.type] ?: return input
            val newIds = input.ids.map { srcId ->
                val newId = rule.mappings.firstNotNullOfOrNull { mapping ->
                    if (srcId in mapping.srcFrom until mapping.srcFrom + mapping.length) {
                        mapping.dstFrom + srcId - mapping.srcFrom
                    } else {
                        null
                    }
                }
                newId ?: srcId
            }
            println("translate $input to $newIds")
            return translate(Product(rule.dst, newIds))
        }

        val location = translate(Product("seed", seeds))
        if (location.type != "location") {
            throw IllegalArgumentException("not a location: $location")
        }
        location.ids.min()!!
    }
}
