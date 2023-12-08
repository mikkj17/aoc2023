import java.io.File

val testInput = """
    Time:      7  15   30
    Distance:  9  40  200
""".trimIndent()

val input = File("input.txt").readText()

private data class Race(val time: Long, val record: Long) {
    fun nWaysToBeatRecord(): Int {
        return (0..time).filter { holdDuration ->
            val raceDuration = time - holdDuration
            val distance = raceDuration * holdDuration
            distance > record
        }.size
    }
}

private fun parse(inp: String): List<Race> {
    val (timesStr, distancesStr) = inp.lines()
    val numberPattern = Regex("""\d+""")
    val times = numberPattern.findAll(timesStr).map { it.value.toLong() }
    val distances = numberPattern.findAll(distancesStr).map { it.value.toLong() }

    return times
        .zip(distances) { time, distance -> Race(time, distance) }
        .toList()
}

private fun first(inp: String): Int {
    return parse(inp)
        .map { race -> race.nWaysToBeatRecord() }
        .reduce { acc, i -> acc * i }
}

private fun second(inp: String): Int {
    return parse(inp.replace(" ", ""))
        .first()
        .nWaysToBeatRecord()
}

println(first(input))
println(second(input))
