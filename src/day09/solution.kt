package day09

import java.io.File

fun parse(inp: String): List<MutableList<Int>> {
    return inp.lines().map { line ->
        line.split(" ").map { it.toInt() }.toMutableList()
    }
}

fun findDifferences(differences: MutableList<MutableList<Int>>): List<MutableList<Int>> {
    val latest = differences.last()
    if (latest.all { it == 0 }) {
        return differences
    }

    differences.add(latest.windowed(2).map { (a, b) -> b - a }.toMutableList())
    return findDifferences(differences)
}

fun computeNextValue(history: MutableList<Int>): Int {
    val differences = findDifferences(mutableListOf(history))
    differences.last().add(0)

    differences.reversed().windowed(2).forEach { (current, next) ->
        next.add(current.last() + next.last())
    }

    return differences.first().last()
}

fun first(inp: String): Int {
    return parse(inp).sumOf { computeNextValue(it) }
}

fun second(inp: String): Int {
    return parse(inp).sumOf { computeNextValue(it.reversed().toMutableList()) }
}

fun main() {
    val testInput = File("src/day09/test-input.txt").readText()
    val input = File("src/day09/input.txt").readText()
    println(first(input))
    println(second(input))
}
