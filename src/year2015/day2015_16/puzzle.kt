package year2015.day2015_16

import puzzle

data class Aunt(
    val number: Int,
    val children: Int? = null,
    val cats: Int? = null,
    val samoyeds: Int? = null,
    val pomeranians: Int? = null,
    val akitas: Int? = null,
    val vizslas: Int? = null,
    val goldfish: Int? = null,
    val trees: Int? = null,
    val cars: Int? = null,
    val perfumes: Int? = null
)

fun main() {
    puzzle(null) { lines ->
        val aunts = lines.mapIndexed { index, it ->
            val map = it.substringAfter(": ").split(", ").map {
                val s = it.split(": ")
                s[0] to s[1].toIntOrNull()
            }.toMap()
            Aunt(
                index + 1,
                map["children"],
                map["cats"],
                map["samoyeds"],
                map["pomeranians"],
                map["akitas"],
                map["vizslas"],
                map["goldfish"],
                map["trees"],
                map["cars"],
                map["perfumes"]
            )
        }
        val evidence = Aunt(
            0,
            children = 3,
            cats = 7,
            samoyeds = 2,
            pomeranians = 3,
            akitas = 0,
            vizslas = 0,
            goldfish = 5,
            trees = 3,
            cars = 2,
            perfumes = 1
        )
        val found = aunts.asSequence().filter { aunt ->
            aunt.children == null || aunt.children == evidence.children
        }.filter { aunt ->
            aunt.cats == null || aunt.cats == evidence.cats
        }.filter { aunt ->
            aunt.samoyeds == null || aunt.samoyeds == evidence.samoyeds
        }.filter { aunt ->
            aunt.pomeranians == null || aunt.pomeranians == evidence.pomeranians
        }.filter { aunt ->
            aunt.akitas == null || aunt.akitas == evidence.akitas
        }.filter { aunt ->
            aunt.vizslas == null || aunt.vizslas == evidence.vizslas
        }.filter { aunt ->
            aunt.goldfish == null || aunt.goldfish == evidence.goldfish
        }.filter { aunt ->
            aunt.trees == null || aunt.trees == evidence.trees
        }.filter { aunt ->
            aunt.cars == null || aunt.cars == evidence.cars
        }.filter { aunt ->
            aunt.perfumes == null || aunt.perfumes == evidence.perfumes
        }.toList()
        found.single().number
    }
}
