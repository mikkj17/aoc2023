import java.io.File

val testInput = """
    seeds: 79 14 55 13

    seed-to-soil map:
    50 98 2
    52 50 48

    soil-to-fertilizer map:
    0 15 37
    37 52 2
    39 0 15

    fertilizer-to-water map:
    49 53 8
    0 11 42
    42 0 7
    57 7 4

    water-to-light map:
    88 18 7
    18 25 70

    light-to-temperature map:
    45 77 23
    81 45 19
    68 64 13

    temperature-to-humidity map:
    0 69 1
    1 0 69

    humidity-to-location map:
    60 56 37
    56 93 4
""".trimIndent()

private data class Almanac(val seeds: Set<Long>, val maps: List<MyMap>) {
    fun findLocationNumber(seed: Long): Long {
        var currentValue = seed

        maps.forEach { map ->
            val range = map.ranges.firstOrNull { it.source <= currentValue && currentValue < it.source + it.length }
            currentValue = if (range == null) currentValue else currentValue + range.destination - range.source
        }

        return currentValue
    }
}

private data class MyMap(val source: String, val destination: String, val ranges: List<Range>)
private data class Range(val destination: Long, val source: Long, val length: Long)

val input = File("input.txt").readText()

private fun parse(inp: String): Almanac {
    val parts = inp.split("\n\n")
    val seeds = parts.first().split(": ").last().split(" ").map { it.toLong() }.toSet()

    val fromToPattern = Regex("""^(\w+)-to-(\w+) map:$""")
    val maps = parts.drop(1).map { part ->
        val lines = part.lines()
        val fromToMatch = fromToPattern.matchEntire(lines.first())
        val (source, destination) = fromToMatch!!.groupValues.drop(1)
        val ranges = lines.drop(1).map { line ->
            val (d, s, l) = line.split(" ").map { it.toLong() }
            Range(d, s, l)
        }

        MyMap(source, destination, ranges)
    }

    return Almanac(seeds, maps)
}

private fun first(inp: String): Long {
    val almanac = parse(inp)

    return almanac.seeds.minOf { almanac.findLocationNumber(it) }
}

private fun second(inp: String): Long {
    val almanac = parse(inp)

    return almanac.seeds.chunked(2).minOf { chunk ->
        val (start, length) = chunk
        (start..<start + length).minOf { almanac.findLocationNumber(it) }
    }
}

println(first(input))
println(second(input))
