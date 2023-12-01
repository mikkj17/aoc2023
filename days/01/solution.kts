import java.io.File

val testInput1 = """
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet
""".trimIndent()

val testInput2 = """
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen
""".trimIndent()

val input = File("input.txt").readText()

private fun first(inp: String): Int {
    return inp.lineSequence().sumOf { line ->
        val digits = line.filter { it.isDigit() }
        "${digits.first()}${digits.last()}".toInt()
    }
}

private fun second(inp: String): Int {
    val numbers = mapOf(
        *(1..9).map { Pair(it.toString(), it.toString()) }.toTypedArray(),
        *listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
            .mapIndexed { i, s -> Pair(s, (i + 1).toString()) }.toTypedArray(),
    )

    return inp.lineSequence().sumOf {
        val first = it.findAnyOf(numbers.keys)!!.second
        val last = it.findLastAnyOf(numbers.keys)!!.second
        "${numbers.getValue(first)}${numbers.getValue(last)}".toInt()
    }
}

println(first(input))
println(second(input))
