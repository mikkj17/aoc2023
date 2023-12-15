import java.io.File

fun first(inp: String): Int {
    return inp.lineSequence().sumOf { line ->
        val digits = line.filter { it.isDigit() }
        "${digits.first()}${digits.last()}".toInt()
    }
}

fun second(inp: String): Int {
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

val testInput = File("test-input.txt").readText()
val testInput2 = File("test-input2.txt").readText()
val input = File("input.txt").readText()
println(first(input))
println(second(input))
