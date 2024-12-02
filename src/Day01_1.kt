import kotlin.math.absoluteValue

fun main() {
    puzzle("01", 11) {
        val list = it
            .map { it.split(" ").filter { it.isNotBlank() } }
        val first = list.map { it[0].toInt() }.sorted()
        val second = list.map { it[1].toInt() }.sorted()
        first.zip(second).map { (a, b) -> (a - b).absoluteValue }.sum()
    }
}
