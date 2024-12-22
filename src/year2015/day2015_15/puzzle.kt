package year2015.day2015_15

import puzzle

//Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
val regex = Regex("""(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""")

data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
)

data class Amount(val ingredient: Ingredient, val amount: Int)

fun main() {
    puzzle(62842880) { lines ->
        val ingredients = lines.map {
            val (name, capacity, durability, flavor, texture, calories) = regex.matchEntire(it)!!.destructured
            Ingredient(name, capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
        }
        val amounts = amounts(100, ingredients)
        amounts.maxOf {
            val capacity = it.sumOf { it.ingredient.capacity * it.amount }
            val durability = it.sumOf { it.ingredient.durability * it.amount }
            val flavor = it.sumOf { it.ingredient.flavor * it.amount }
            val texture = it.sumOf { it.ingredient.texture * it.amount }
            if (capacity > 0 && durability > 0 && flavor > 0 && texture > 0) {
                capacity * durability * flavor * texture
            } else {
                0
            }
        }
    }
}

private fun amounts(teaspoonsLeft: Int, ingredients: List<Ingredient>): List<List<Amount>> {
    if (ingredients.size == 1) {
        return listOf(listOf(Amount(ingredients.first(), teaspoonsLeft)))
    }
    val first = ingredients.first()
    return (0..teaspoonsLeft).flatMap {
        val amount = Amount(first, it)
        amounts(teaspoonsLeft - it, ingredients.drop(1)).map { listOf(amount) + it }
    }
}
