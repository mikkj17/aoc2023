package day19

import java.io.File

data class Rule(val category: Char, val comparator: Char, val value: Int, val destination: String) {
    fun compare(otherValue: Int) = when (comparator) {
        '<' -> otherValue < value
        '>' -> otherValue > value
        else -> throw AssertionError("unknown comparator $comparator")
    }

    companion object {
        private val pattern = Regex("""(\w)([<>])(\d+):(\w+)""")
        fun create(inp: String): Rule {
            val (category, comparator, value, destination) = pattern.matchEntire(inp)!!.groupValues.drop(1)
            return Rule(category.first(), comparator.first(), value.toInt(), destination)
        }
    }
}

data class Workflow(val name: String, val rules: List<Rule>, val destination: String) {
    companion object {
        private val pattern = Regex("""(\w+)\{(.*)}""")
        fun create(inp: String): Workflow {
            val (name, rest) = pattern.matchEntire(inp)!!.groupValues.drop(1)
            val rules = rest.split(",")
            return Workflow(name, rules.dropLast(1).map { Rule.create(it) }, rules.last())
        }
    }
}

fun parse(inp: String): Pair<List<Workflow>, List<Map<Char, Int>>> {
    val groups = inp.split("\n\n")
    val workflows = groups.first().lines().map { Workflow.create(it) }

    val partPattern = Regex("""(\w)=(\d+)""")
    val parts = groups.last().lines().map { line ->
        partPattern.findAll(line).associate {
            val (category, value) = it.groupValues.drop(1)
            category.first() to value.toInt()
        }
    }

    return workflows to parts
}

fun isAccepted(part: Map<Char, Int>, workflows: List<Workflow>): Boolean {
    var state = "in"
    while (state !in "AR") {
        val workflow = workflows.first { it.name == state }
        var found = false
        for (rule in workflow.rules) {
            if (rule.compare(part.getValue(rule.category))) {
                state = rule.destination
                found = true
                break
            }
        }
        if (!found) {
            state = workflow.destination
        }
    }

    return state == "A"
}

fun first(inp: String): Int {
    val (workflows, parts) = parse(inp)
    return parts
        .filter { isAccepted(it, workflows) }
        .sumOf { it.values.sum() }
}

fun count(name: String, ranges: MutableMap<Char, Pair<Long, Long>>, workflows: List<Workflow>): Long {
    // branched into rejected state - return 0
    if (name == "R") {
        return 0
    }

    // branched into accepted state - return the product of the diff between low and high for each range
    if (name == "A") {
        return ranges.values
            .map { (low, high) -> high - low + 1 }
            .reduce { acc, i -> acc * i }
    }

    var total = 0L
    val workflow = workflows.first { it.name == name }
    for (rule in workflow.rules) {
        val value = rule.value.toLong()
        val (low, high) = ranges.getValue(rule.category)

        // using the rule, split the range into a true side and a false side
        val (truePart, falsePart) = if (rule.comparator == '<') {
            Pair(low to value - 1, value to high)
        } else {
            Pair(value + 1 to high, low to value)
        }

        // replace the range with the true part and recurse on that
        total += count(
            rule.destination,
            ranges.toMutableMap().apply { replace(rule.category, truePart) },
            workflows,
        )

        // replace the range with the false part for next iterations
        ranges.replace(rule.category, falsePart)
    }

    return total + count(workflow.destination, ranges, workflows)
}

fun second(inp: String): Long {
    val (workflows, _) = parse(inp)
    val part = "xmas".associateWith { 1L to 4000L }.toMutableMap()
    return count("in", part, workflows)
}

fun main() {
    val testInput = File("src/day19/test-input.txt").readText()
    val input = File("src/day19/input.txt").readText()
    println(first(input))
    println(second(input))
}
