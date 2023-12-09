import java.io.File

val testInput = """
    0 3 6 9 12 15
    1 3 6 10 15 21
    10 13 16 21 30 45
""".trimIndent()

val input = File("input.txt").readText()

private fun parse(inp: String): List<MutableList<Int>> {
    return inp.lines().map { line ->
        line.split(" ").map { it.toInt() }.toMutableList()
    }
}

private fun findDifferences(differences: MutableList<MutableList<Int>>): List<MutableList<Int>> {
    val latest = differences.last()
    if (latest.all { it == 0 }) {
        return differences
    }

    differences.add(latest.windowed(2).map { (a, b) -> b - a }.toMutableList())
    return findDifferences(differences)
}

private fun computeNextValue(history: MutableList<Int>): Int {
    val differences = findDifferences(mutableListOf(history))
    differences.last().add(0)

    differences.reversed().windowed(2).forEach { (current, next) ->
        next.add(current.last() + next.last())
    }

    return differences.first().last()
}

private fun first(inp: String): Int {
    return parse(inp).sumOf { computeNextValue(it) }
}

private fun second(inp: String): Int {
    return parse(inp).sumOf { computeNextValue(it.reversed().toMutableList()) }
}

println(first(input))
println(second(input))
