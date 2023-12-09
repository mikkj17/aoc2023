import java.io.File
import kotlin.math.max

val testInput = """
    RL

    AAA = (BBB, CCC)
    BBB = (DDD, EEE)
    CCC = (ZZZ, GGG)
    DDD = (DDD, DDD)
    EEE = (EEE, EEE)
    GGG = (GGG, GGG)
    ZZZ = (ZZZ, ZZZ)
""".trimIndent()

val testInput2 = """
    LLR

    AAA = (BBB, BBB)
    BBB = (AAA, ZZZ)
    ZZZ = (ZZZ, ZZZ)
""".trimIndent()

val testInput3 = """
    LR

    11A = (11B, XXX)
    11B = (XXX, 11Z)
    11Z = (11B, XXX)
    22A = (22B, XXX)
    22B = (22C, 22C)
    22C = (22Z, 22Z)
    22Z = (22B, 22B)
    XXX = (XXX, XXX)
""".trimIndent()

val input = File("input.txt").readText()

private data class Network(
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

private fun parse(inp: String): Network {
    val (instructions, networkString) = inp.split("\n\n")
    val nodes = Regex("""(\w{3}) = \((\w{3}), (\w{3})\)""").findAll(networkString).associate { matchResult ->
        val (from, left, right) = matchResult.groupValues.drop(1)
        Pair(from, Pair(left, right))
    }

    return Network(instructions, nodes)
}

private fun first(inp: String): Long {
    val network = parse(inp)

    return network.stepsToGoal("AAA") { it == "ZZZ" }
}

private fun lcm(a: Long, b: Long): Long {
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

private fun second(inp: String): Long {
    val network = parse(inp)

    return network.nodes.keys
        .filter { it.last() == 'A' }
        .map { network.stepsToGoal(it) { pos -> pos.last() == 'Z' } }
        .reduce { acc, x -> lcm(acc, x) }
}

println(first(input))
println(second(input))
