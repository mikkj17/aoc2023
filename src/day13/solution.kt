package day13

import utils.transpose
import java.io.File
import kotlin.math.min

typealias Comparison = (above: List<List<Char>>, below: List<List<Char>>) -> Boolean

fun findMirrorIndex(
    grid: List<List<Char>>,
    comparison: Comparison,
): Int {
    for (i in 1..<grid.size) {
        val above = grid.take(i).reversed()
        val below = grid.drop(i)

        if (comparison(above, below)) {
            return i
        }
    }

    return 0
}

fun compute(inp: String, comparison: Comparison): Int {
    return inp.split("\n\n").sumOf { part ->
        val grid = part.lines().map { it.toCharArray().toList() }
        findMirrorIndex(grid, comparison) * 100 + findMirrorIndex(grid.transpose(), comparison)
    }
}

fun first(inp: String): Int {
    return compute(inp) { above, below ->
        val size = min(above.size, below.size)
        above.take(size) == below.take(size)
    }
}

fun second(inp: String): Int {
    return compute(inp) { above, below ->
        above.zip(below).sumOf { (a, b) ->
            a.zip(b).filter { (x, y) -> x != y }.size
        } == 1
    }
}

fun main() {
    val testInput = File("src/day13/test-input.txt").readText()
    val input = File("src/day13/input.txt").readText()
    println(first(input))
    println(second(input))
}
