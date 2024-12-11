package year2023.day2023_05

import puzzle

data class Mapping(val src: LongRange, val dst: LongRange, val delta: Long)

data class SubRanges(val included: List<LongRange>, val excluded: List<LongRange>)

fun cut(range: LongRange, cutter: LongRange): SubRanges {
    if (cutter.last < range.first || cutter.first > range.last) {
        // cutter outside range
        return SubRanges(emptyList(), listOf(range))
    }
    if (cutter.first <= range.first && cutter.last >= range.last) {
        // cutter includes range
        return SubRanges(listOf(range), emptyList())
    }
    // cutter intersects range
    if (cutter.first > range.first && cutter.last < range.last) {
        return SubRanges(
            listOf(cutter),
            listOf(
                range.first until cutter.first,
                cutter.last + 1..range.last
            )
        )
    }
    // cutter at start
    if (cutter.first <= range.first) {
        return SubRanges(
            listOf(cutter),
            listOf(cutter.last + 1..range.last)
        )
    }
    // cutter at end
    return SubRanges(
        listOf(cutter),
        listOf(range.first until cutter.first)
    )
}

data class Rule(val src: String, val dst: String, val mappings: MutableList<Mapping>)

data class Product(val type: String, val ids: List<LongRange>)

fun main() {
    puzzle(46) { lines ->
        val headerRegex = "(.+)-to-(.+) map:".toRegex()
        val seeds = lines[0].substringAfter(":").trim().split(" ").map { it.toLong() }
            .chunked(2)
            .map {
                val from = it[0]
                from until from + it[1]
            }
        val rules = mutableListOf<Rule>()
        lines.drop(1).forEach { line ->
            val header = headerRegex.matchEntire(line)
            if (header != null) {
                val (src, dst) = header.destructured
                rules.add(Rule(src, dst, mutableListOf()))
            } else {
                val (dstFrom, srcFrom, length) = line.split(" ").map { it.toLong() }
                val src = srcFrom until srcFrom + length
                val dst = dstFrom until dstFrom + length
                val delta = dstFrom - srcFrom
                rules.last().mappings.add(Mapping(src, dst, delta))
            }
        }
        val ruleMap = rules.associateBy { it.src }

        fun translate(input: Product): Product {
            val rule = ruleMap[input.type] ?: return input
            val newIds = input.ids.flatMap { srcRange ->
                rule.mappings.flatMap { mapping ->
                    val cut = cut(srcRange, mapping.src)
                    val r: List<LongRange> = cut.excluded + cut.included.map { included ->
                        included.first + mapping.delta..included.last + mapping.delta
                    }
                    r
                }
            }
            println("translate $input to $newIds")
            return translate(Product(rule.dst, newIds))
        }

        val location = translate(Product("seed", seeds))
        if (location.type != "location") {
            throw IllegalArgumentException("not a location: $location")
        }
        location.ids.minOf { it.first }
    }
}
