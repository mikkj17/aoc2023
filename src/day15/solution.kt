package day15

import java.io.File

fun hash(value: String): Int {
    var digest = 0
    value.forEach {
        digest += it.code
        digest *= 17
        digest %= 256
    }
    return digest
}

fun first(inp: String): Int {
    return inp.split(",").sumOf { hash(it) }
}

fun second(inp: String): Int {
    val boxes = List(256) { mutableMapOf<String, Int>() }
    Regex("""(\w+)([=\-])(\d?)""").findAll(inp).forEach { step ->
        val (label, operation, focal) = step.groupValues.drop(1)
        val box = boxes[hash(label)]

        if (operation == "-") {
            box.remove(label)
        }
        else {
            box[label] = focal.toInt()
        }
    }

    return boxes.withIndex().sumOf { (boxIndex, box) ->
        box.values.withIndex().sumOf { (slotIndex, focal) ->
            (boxIndex + 1) * (slotIndex + 1) * focal
        }
    }
}

fun main() {
    val testInput = File("src/day15/test-input.txt").readText()
    val input = File("src/day15/input.txt").readText()
    println(first(input))
    println(second(input))
}
