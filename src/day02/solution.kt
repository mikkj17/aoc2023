package day02

import java.io.File

data class Cube(val color: String, val count: Int)
data class Game(val id: Int, val cubes: Sequence<Cube>)

fun parseToGames(inp: String): Sequence<Game> {
    val cubePattern = Regex("""(\d+) (red|green|blue)""")
    return inp.lineSequence().map { line ->
        val (gameStr, cubesStr) = line.split(": ")
        val gameId = gameStr.split(" ").last().toInt()
        val cubes = cubePattern.findAll(cubesStr).map {
            Cube(it.groupValues[2], it.groupValues[1].toInt())
        }

        Game(gameId, cubes)
    }
}

fun first(inp: String): Int {
    val limits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    return parseToGames(inp).filter { game ->
        game.cubes.none { it.count > limits.getValue(it.color) }
    }.sumOf { it.id }
}

fun second(inp: String): Int {
    return parseToGames(inp).sumOf { game ->
        game.cubes.groupBy { it.color }
            .map { group -> group.value.maxOf { it.count } }
            .reduce { acc, i -> acc * i }
    }
}

fun main() {
    val testInput = File("src/day02/test-input.txt").readText()
    val input = File("src/day02/input.txt").readText()
    println(first(input))
    println(second(input))
}
