import Solution.Position
import java.io.File
import kotlin.math.abs

typealias Position = Pair<Int, Int>

data class Galaxy(val pos: Position) {
    fun distance(other: Galaxy): Long {
        return abs(pos.first - other.pos.first) + abs(pos.second - other.pos.second).toLong()
    }
}

fun noGalaxyIndices(inp: List<String>): List<Int> {
    return inp.withIndex()
        .filter { (_, row) -> row.none { it == '#' } }
        .map { (i, _) -> i }
}

fun findGalaxies(
    rows: List<String>,
    rowsWithoutGalaxy: List<Int>,
    colsWithoutGalaxy: List<Int>,
    expansionFactor: Int,
): List<Galaxy> {
    val galaxies = mutableListOf<Galaxy>()

    rows.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == '#') {
                val rowsBefore = rowsWithoutGalaxy.filter { it < y }.size
                val colsBefore = colsWithoutGalaxy.filter { it < x }.size
                val pos = Pair(
                    y - rowsBefore + rowsBefore * expansionFactor,
                    x - colsBefore + colsBefore * expansionFactor,
                )
                galaxies.add(Galaxy(pos))
            }
        }
    }

    return galaxies
}

fun compute(inp: String, expansionFactor: Int): Long {
    val rows = inp.lines()
    val cols = rows.first().indices.map { index ->
        rows.map { it[index] }.joinToString("")
    }
    val rowsWithoutGalaxy = noGalaxyIndices(rows)
    val colsWithoutGalaxy = noGalaxyIndices(cols)
    val galaxies = findGalaxies(rows, rowsWithoutGalaxy, colsWithoutGalaxy, expansionFactor)

    return galaxies.mapIndexed { index, galaxy ->
        galaxies.drop(index).sumOf { galaxy.distance(it) }
    }.sum()
}

fun first(inp: String) = compute(inp, 2)

fun second(inp: String) = compute(inp, 1_000_000)

val testInput = File("test-input.txt").readText()
val input = File("input.txt").readText()
println(first(input))
println(second(input))
