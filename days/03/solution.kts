import Solution.Position
import java.io.File
import kotlin.math.abs

val testInput = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...$.*....
    .664.598..
""".trimIndent()

val input = File("input.txt").readText()

private typealias Position = Pair<Int, Int>

private data class Num(val number: Int, val start: Position, val end: Position)
private data class Symbol(val symbol: Char, val position: Position)

private fun parse(inp: String): Pair<List<Num>, List<Symbol>> {
    val numbers = mutableListOf<Num>()
    val symbols = mutableListOf<Symbol>()

    inp.lineSequence().forEachIndexed { y, row ->
        var partialNumber = ""
        for ((x, char) in row.withIndex()) {
            if (char.isDigit()) {
                partialNumber += char
                continue
            }

            if (partialNumber.isNotBlank()) {
                numbers.add(
                    Num(
                        number = partialNumber.toInt(),
                        start = Pair(y, x - partialNumber.length),
                        end = Pair(y, x - 1)
                    )
                )
            }

            if (char != '.') {
                symbols.add(Symbol(char, Pair(y, x)))
            }

            partialNumber = ""
        }

        if (partialNumber.isNotBlank()) {
            numbers.add(
                Num(
                    number = partialNumber.toInt(),
                    start = Pair(y, row.length - partialNumber.length),
                    end = Pair(y, row.length - 1)
                )
            )
        }
    }

    return Pair(numbers, symbols)
}

private fun isAdjacent(num: Num, symbol: Symbol): Boolean {
    return abs(symbol.position.first - num.start.first) <= 1 && (
            abs(symbol.position.second - num.start.second) <= 1 ||
                    abs(symbol.position.second - num.end.second) <= 1
            )
}

private fun first(inp: String): Int {
    val (numbers, symbols) = parse(inp)

    return numbers.filter { num ->
        symbols.any { symbol -> isAdjacent(num, symbol) }
    }.sumOf { it.number }
}

private fun second(inp: String): Int {
    val (numbers, symbols) = parse(inp)

    val gears = symbols.filter { it.symbol == '*' }
        .map { symbol -> numbers.filter { num -> isAdjacent(num, symbol) } }
        .filter { it.size == 2 }

    return gears.sumOf { it.first().number * it.last().number }
}

println(first(input))
println(second(input))
