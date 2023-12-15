import java.io.File

fun first(inp: String): Int {
    throw NotImplementedError()
}

fun second(inp: String): Int {
    throw NotImplementedError()
}

val testInput = File("test-input.txt").readText()
val input = File("input.txt").readText()
println(first(testInput))
println(second(testInput))
