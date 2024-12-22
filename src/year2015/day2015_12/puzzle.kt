package year2015.day2015_12

import kotlinx.serialization.json.*
import puzzle

fun main() {
    puzzle(4) { lines ->
        val input = lines[0]
        val obj = Json.parseToJsonElement(input)
        sum(obj)
    }
}

fun sum(obj: JsonElement): Int {
    var sum = 0
    when (obj) {
        is JsonObject -> {
            if (obj.values.any { isRed(it) }) {
                return 0
            }
            sum += obj.values.sumOf { sum(it) }
        }
        is JsonArray -> {
            sum += obj.sumOf { sum(it) }
        }
        is JsonPrimitive -> {
            if (obj.isString) {
                return 0
            }
            sum += obj.intOrNull ?: 0
        }
    }
    return sum
}

private fun isRed(it: JsonElement) =
    (it as? JsonPrimitive)?.let { it.content } == "red"
