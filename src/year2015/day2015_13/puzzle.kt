package year2015.day2015_13

import permutations
import puzzle

// Alice would gain 54 happiness units by sitting next to Bob.
val regex = Regex("(.+) would (gain|lose) (\\d+) happiness units by sitting next to (.+)\\.")

data class Constraint(val a: String, val b: String, val mood: Int)

fun main() {
    puzzle(null) { lines ->
        val constraints = lines.map {
            regex.find(it)!!.destructured.let { (a, sign, n, b) ->
                Constraint(a, b, if (sign == "gain") n.toInt() else -n.toInt())
            }
        }.toMutableList()

        val others = constraints.flatMap { listOf(it.a, it.b) }.toSet()
        others.forEach { person ->
            constraints.add(Constraint("Me", person, 0))
            constraints.add(Constraint(person, "Me", 0))
        }

        val people = constraints.flatMap { listOf(it.a, it.b) }.toSet()

        people.permutations().maxOf { seating ->
            seating.windowed(2).sumOf { (a, b) ->
                constraints.filter { it.a == a && it.b == b || it.a == b && it.b == a }.sumOf { it.mood }
            } + constraints.filter { it.a == seating.first() && it.b == seating.last() || it.a == seating.last() && it.b == seating.first() }.sumOf { it.mood }
        }
    }
}
