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
            listOf(range.first .. cutter.last),
            listOf(cutter.last + 1..range.last)
        )
    }
    // cutter at end
    return SubRanges(
        listOf(cutter.first .. range.last),
        listOf(range.first..<cutter.first)
    )
}

data class Rule(val src: String, val dst: String, val mappings: MutableList<Mapping>)

data class Product(val type: String, val ranges: List<Translation>)

data class Translation(
    val src: LongRange,
    val dst: LongRange,
    val srcProduct: String?,
    val dstProduct: String,
    val parent: Translation?,
    val mapping: Mapping?
)

fun main() {
    puzzle(46) { lines ->
        val headerRegex = "(.+)-to-(.+) map:".toRegex()
        val seeds = lines[0].substringAfter(":").trim().split(" ").map { it.toLong() }
            .chunked(2)
            .map {
                val from = it[0]
                val range = from until from + it[1]
                Translation(range, range, null, "seed", null, null)
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
//            println("translate $input")
            val rule = ruleMap[input.type] ?: return input
//            println(rule)
            val newIds = input.ranges.flatMap { range ->
                val untranslated = mutableListOf(range.dst)
                val translated = mutableListOf<Translation>()
                rule.mappings.forEach { mapping ->
                    val l = untranslated.toList()
                    untranslated.clear()
                    l.forEach { r ->
                        val cut = cut(r, mapping.src)
                        untranslated.addAll(cut.excluded)
                        translated.addAll(cut.included.map { included ->
                            Translation(included, included.first + mapping.delta..included.last + mapping.delta,
                                rule.src, rule.dst, range, mapping)
                        })
                    }
                }
                translated + untranslated.map { Translation(it, it, rule.src, rule.dst, range, null) }
            }
//            println("translate $input to $newIds")
            return translate(Product(rule.dst, newIds))
        }

        val location = translate(Product("seed", seeds))
        if (location.type != "location") {
            throw IllegalArgumentException("not a location: $location")
        }
        val min = location.ranges.minBy { it.dst.first }
        val parents = parents(min)
        parents.forEach {
            println("${it.dst.last - it.dst.first + 1} ${it.srcProduct} ${it.src} -> ${it.dstProduct} ${it.dst} ${it.mapping}")
        }
        min.dst.first
    }
}

fun parents(t: Translation?): List<Translation> {
    if (t == null) return emptyList()
    return parents(t.parent) + listOf(t)
}
