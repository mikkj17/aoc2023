import java.io.File
import kotlin.math.max

data class Network(
    val instructions: String,
    val nodes: Map<String, Pair<String, String>>
) {
    fun stepsToGoal(start: String, isGoal: (String) -> Boolean): Long {
        var pos = start
        var steps: Long = 0

        while (!isGoal(pos)) {
            val instruction = instructions[(steps % instructions.length.toLong()).toInt()]
            val paths = nodes.getValue(pos)
            pos = if (instruction == 'L') paths.first else paths.second

            steps++
        }

        return steps
    }
}

fun parse(inp: String): Network {
    val (instructions, networkString) = inp.split("\n\n")
    val nodes = Regex("""(\w{3}) = \((\w{3}), (\w{3})\)""").findAll(networkString).associate { matchResult ->
        val (from, left, right) = matchResult.groupValues.drop(1)
        Pair(from, left to right)
    }

    return Network(instructions, nodes)
}

fun first(inp: String): Long {
    val network = parse(inp)

    return network.stepsToGoal("AAA") { it == "ZZZ" }
}

fun lcm(a: Long, b: Long): Long {
    val larger = max(a, b)
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == (0).toLong() && lcm % b == (0).toLong()) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun second(inp: String): Long {
    val network = parse(inp)

    return network.nodes.keys
        .filter { it.last() == 'A' }
        .map { network.stepsToGoal(it) { pos -> pos.last() == 'Z' } }
        .reduce { acc, x -> lcm(acc, x) }
}

val testInput = File("test-input.txt").readText()
val input = File("input.txt").readText()
println(first(input))
println(second(input))
