import java.io.File

data class Almanac(val seeds: Set<Long>, val maps: List<List<Range>>) {
    fun findLocationNumber(seed: Long): Long {
        var currentValue = seed

        maps.forEach { map ->
            val range = map.firstOrNull { it.source <= currentValue && currentValue < it.source + it.length }
            currentValue = if (range == null) currentValue else currentValue + range.destination - range.source
        }

        return currentValue
    }
}

data class Range(val destination: Long, val source: Long, val length: Long)

fun parse(inp: String): Almanac {
    val parts = inp.split("\n\n")
    val seeds = parts.first().split(": ").last().split(" ").map { it.toLong() }.toSet()

    val maps = parts.drop(1).map { part ->
        part.lines().drop(1).map { line ->
            val (d, s, l) = line.split(" ").map { it.toLong() }
            Range(d, s, l)
        }
    }

    return Almanac(seeds, maps)
}

fun first(inp: String): Long {
    val almanac = parse(inp)

    return almanac.seeds.minOf { almanac.findLocationNumber(it) }
}

fun second(inp: String): Long {
    val almanac = parse(inp)

    return almanac.seeds.chunked(2).minOf { chunk ->
        val (start, length) = chunk
        (start..<start + length).minOf { almanac.findLocationNumber(it) }
    }
}

val testInput = File("test-input.txt").readText()
val input = File("input.txt").readText()
println(first(input))
println(second(input))
