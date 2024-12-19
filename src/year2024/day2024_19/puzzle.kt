package year2024.day2024_19

import puzzle

data class Trie(val children: MutableMap<Char, Trie> = mutableMapOf(), var isWord: Boolean = false) {
    fun add(word: String) {
        var current = this
        for (c in word) {
            current = current.children.getOrPut(c) { Trie() }
        }
        current.isWord = true
    }

    fun findAllSubstrings(word: String): Set<Int> {
        val result = mutableListOf<Int>()
        var current = this
        for (i in word.indices) {
            current = current.children[word[i]] ?: break
            if (current.isWord) {
                result.add(i + 1)
            }
        }
        return result.toSet()
    }
}

data class Onsen(val root: Trie, val cache: MutableMap<String, Long> = mutableMapOf())

fun find(trie: Trie, word: String, found: List<String>, onsen: Onsen): Long {
    if (word in onsen.cache) {
        return onsen.cache[word]!!
    }
    val choices = trie.findAllSubstrings(word).sumOf {
        val substring = word.substring(it)
        if (substring.isEmpty()) {
            val found = found + word
            println("Found: $found")
            1L
        } else {
            find(trie, substring, found + word.substring(0, it), onsen)
        }
    }
    onsen.cache[word] = choices
    return choices
}

fun main() {
    puzzle(16) { lines ->
        val trie = Trie()
        val onsen = Onsen(trie)
        lines[0].split(", ").forEach { trie.add(it) }
        lines.drop(1).sumOf { find(trie, it, emptyList(), onsen) }
    }
}
