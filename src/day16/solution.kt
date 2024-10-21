package day16

import java.io.File

typealias Position = Pair<Int, Int>

typealias Direction = Pair<Int, Int>

typealias Beam = Pair<Position, Direction>

data class Solver(val tiles: List<String>, val startingConfiguration: Beam) {
    private val beams = mutableListOf(startingConfiguration)
    private val energized = mutableSetOf<Beam>()

    private fun isWithing(y: Int, x: Int): Boolean {
        return 0 <= y && y < tiles.size
                && 0 <= x && x < tiles.first().length
    }

    private fun getDirections(tile: Char, dy: Int, dx: Int) = when {
        tile == '.' || (tile == '-' && dx == 1) || (tile == '|' && dy == 1) -> {
            listOf(dy to dx)
        }

        tile == '/' -> {
            listOf(-dx to -dy)
        }

        tile == '\\' -> {
            listOf(dx to dy)
        }

        tile == '|' -> {
            listOf(-1 to 0, 1 to 0)
        }

        tile == '-' -> {
            listOf(0 to -1, 0 to 1)
        }

        else -> throw AssertionError("shouldn't happen")
    }

    fun run(): Int {
        while (beams.isNotEmpty()) {
            val (position, direction) = beams.removeFirst()
            var (y, x) = position
            val (dy, dx) = direction
            y += dy
            x += dx

            if (!isWithing(y, x)) {
                continue
            }

            getDirections(tiles[y][x], dy, dx).forEach { (dy, dx) ->
                val beam = Pair(y, x) to Pair(dy, dx)
                if (beam !in energized) {
                    beams.add(beam)
                    energized.add(beam)
                }
            }
        }

        return energized
            .distinctBy { it.first }
            .size
    }
}

fun first(inp: String): Int {
    return Solver(inp.lines(), Pair(0, -1) to Pair(0, 1)).run()
}

fun second(inp: String): Int {
    val lines = inp.lines()
    val configs = mutableListOf<Beam>()

    for (y in -1..lines.size) {
        configs.add(Pair(y, -1) to Pair(0, 1))
        configs.add(Pair(y, lines.first().length) to Pair(0, -1))
    }

    for (x in -1..lines.first().length) {
        configs.add(Pair(-1, x) to Pair(1, 0))
        configs.add(Pair(lines.size, x) to Pair(-1, 0))
    }

    return configs.maxOf { Solver(lines, it).run() }
}

fun main() {
    val testInput = File("src/day16/test-input.txt").readText()
    val input = File("src/day16/input.txt").readText()
    println(first(input))
    println(second(input))
}
