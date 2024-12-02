fun main() {
    puzzle("01", 31) {
        val list = it
            .map { it.split(" ").filter { it.isNotBlank() } }
        val first = list.map { it[0].toInt() }.sorted()
        val second = list.map { it[1].toInt() }.sorted()
        first.map { f -> f * second.filter { it == f }.count() }.sum()
    }
}
