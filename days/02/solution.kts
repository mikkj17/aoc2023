import java.io.File

val testInput = """
    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
""".trimIndent()

val input = File("input.txt").readText()

private data class Cube(val color: String, val count: Int)
private data class Game(val id: Int, val cubes: Sequence<Cube>)

private fun parseToGames(inp: String): Sequence<Game> {
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

private fun first(inp: String): Int {
    val limits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    return parseToGames(inp).filter { game ->
        game.cubes.none { it.count > limits.getValue(it.color) }
    }.sumOf { it.id }
}

private fun second(inp: String): Int {
    return parseToGames(inp).sumOf { game ->
        game.cubes.groupBy { it.color }
            .map { group -> group.value.maxOf { it.count } }
            .reduce { acc, i -> acc * i }
    }
}

println(first(input))
println(second(input))
