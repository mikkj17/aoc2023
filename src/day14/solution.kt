package day14

import utils.transpose
import java.io.File

fun move(grid: List<String>, reverse: Boolean): List<String> {
    return grid.map { row ->
        row.split("#").joinToString("#") { group ->
            val sorted = if (reverse) group.toCharArray().sorted() else group.toCharArray().sortedDescending()
            sorted.joinToString("")
        }
    }
}

fun computeLoad(grid: List<String>): Int {
    return grid.withIndex().sumOf { (index, row) ->
        (grid.size - index) * row.count { it == 'O' }
    }
}

fun first(inp: String): Int {
    return computeLoad(move(inp.lines().transpose(), false).transpose())
}

fun second(inp: String): Int {
    var grid = inp.lines()
    val seen = mutableSetOf(grid)
    val states = mutableListOf(grid)

    var cycles = 1
    while (true) {
        grid = move(grid.transpose(), false).transpose()
        grid = move(grid, false)
        grid = move(grid.transpose(), true).transpose()
        grid = move(grid, true)

        if (grid in seen) {
            break
        }

        seen.add(grid)
        states.add(grid)
        cycles++
    }

    val start = states.indexOf(grid)
    return computeLoad(states[(1_000_000_000 - start) % (cycles - start) + start])
}

fun main() {
    val testInput = File("src/day14/test-input.txt").readText()
    val input = File("src/day14/input.txt").readText()
    println(first(input))
    println(second(input))
}
