package day06

import java.io.File

data class Race(val time: Long, val record: Long) {
    fun waysToBeatRecord(): Int {
        return (0..time).filter { holdDuration ->
            val raceDuration = time - holdDuration
            val distance = raceDuration * holdDuration
            distance > record
        }.size
    }
}

fun parse(inp: String): List<Race> {
    val (timesStr, distancesStr) = inp.lines()
    val numberPattern = Regex("""\d+""")
    val times = numberPattern.findAll(timesStr).map { it.value.toLong() }
    val distances = numberPattern.findAll(distancesStr).map { it.value.toLong() }

    return times
        .zip(distances) { time, distance -> Race(time, distance) }
        .toList()
}

fun first(inp: String): Int {
    return parse(inp)
        .map { race -> race.waysToBeatRecord() }
        .reduce { acc, i -> acc * i }
}

fun second(inp: String): Int {
    return parse(inp.replace(" ", ""))
        .first()
        .waysToBeatRecord()
}

fun main() {
    val testInput = File("src/day06/test-input.txt").readText()
    val input = File("src/day06/input.txt").readText()
    println(first(input))
    println(second(input))
}
