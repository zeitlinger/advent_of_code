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

data class Onsen(val root: Trie, val cache: MutableMap<String, Boolean> = mutableMapOf())

fun find(trie: Trie, word: String, found: List<String>, onsen: Onsen): Boolean {
    if (word in onsen.cache) {
        return onsen.cache[word]!!
    }
    trie.findAllSubstrings(word).forEach {
        val substring = word.substring(it)
        if (substring.isEmpty()) {
            val found = found + word
            println("Found: $found")
            return true
        }
        if (find(trie, substring, found + word.substring(0, it), onsen)) {
            return true
        }
    }
    onsen.cache[word] = false
    return false
}

fun main() {
    puzzle(6) { lines ->
        val trie = Trie()
        val onsen = Onsen(trie)
        lines[0].split(", ").forEach { trie.add(it) }
        lines.drop(1).count { find(trie, it, emptyList(), onsen) }
    }
}
