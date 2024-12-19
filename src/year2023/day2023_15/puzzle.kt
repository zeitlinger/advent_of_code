package year2023.day2023_15

import puzzle

data class Lens(val label: String, val focalLength: Int)

data class HashMap(val boxes: MutableList<MutableList<Lens>> = MutableList(256) { mutableListOf() }) {
    fun addLens(lens: Lens) {
        val box = boxes[hash(lens.label)]
        val slot = box.indexOfFirst { it.label == lens.label }
        if (slot != -1) {
            box[slot] = lens
            return
        }
        box.add(lens)
    }

    fun removeLens(label: String) {
        boxes[hash(label)].removeAll { it.label == label }
    }
}

fun main() {
    puzzle(145) { lines ->
        val hashMap = HashMap()
        lines[0].split(",").forEach { command ->
            if (command.endsWith("-")) {
                val label = command.dropLast(1)
                hashMap.removeLens(label)
            } else {
                val s = command.split("=")
                val label = s[0]
                val focalLength = s[1].toInt()
                hashMap.addLens(Lens(label, focalLength))
            }
        }
        hashMap.boxes.mapIndexed { box, list ->
            list.mapIndexed { slot, lens -> lens.focalLength * (box + 1) * (slot + 1) }
        }.flatten().sum()
    }
}

fun hash(s: String): Int {
    var current = 0
    for (c in s) {
        val code = c.code
        current += code
        current *= 17
        current %= 256
    }
    return current
}
