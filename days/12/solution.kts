import java.io.File

data class ConditionRecord(val springs: String, val groups: List<Int>) {
    companion object {
        private val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

        private fun count(springs: String, groups: List<Int>): Long {
            if (springs.isEmpty()) {
                return if (groups.isEmpty()) 1 else 0
            }

            if (groups.isEmpty()) {
                return if ("#" in springs) 0 else 1
            }

            return cache.getOrPut(springs to groups) {
                var result = 0L

                val firstChar = springs.first()
                // operational: remove first spring and check the rest
                if (firstChar in ".?") {
                    result += count(springs.drop(1), groups)
                }

                val firstGroup = groups.first()

                // start of a group
                if (firstChar in "#?") {
                    // there must be enough springs left (e.g. cannot be ?## 4)
                    if (firstGroup <= springs.length) {
                        // no operational springs in length `firstGroup` of springs
                        if (!springs.take(firstGroup).contains('.')) {
                            // next spring after `firstGroup` must be operational (or we're at end of springs)
                            if (springs.getOrNull(firstGroup) != '#') {
                                result += count(springs.drop(firstGroup + 1), groups.drop(1))
                            }
                        }
                    }
                }

                result
            }
        }
    }

    fun computeArrangements() = count(springs, groups)
}

fun parse(inp: String, unfolds: Int): List<ConditionRecord> {
    return inp.lines().map { line ->
        var (springs, groupsString) = line.split(" ")
        var groups = groupsString.split(",").map { it.toInt() }
        springs = List(unfolds) { springs }.joinToString("?")
        groups = List(unfolds) { groups }.flatten()

        ConditionRecord(springs, groups)
    }
}

fun computeArrangements(inp: String, unfolds: Int) = parse(inp, unfolds).sumOf { it.computeArrangements() }

fun first(inp: String) = computeArrangements(inp, 1)
fun second(inp: String) = computeArrangements(inp, 5)

val testInput = File("test-input.txt").readText()
val input = File("input.txt").readText()
println(first(input))
println(second(input))
