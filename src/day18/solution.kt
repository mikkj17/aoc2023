package day18

import java.io.File
import kotlin.math.abs

typealias Position = Pair<Long, Long>

typealias Direction = Pair<Int, Int>

data class Instruction(val direction: Direction, val steps: Int)

fun String.asDirection() = when (this) {
    in "U3" -> -1 to 0
    in "D1" -> 1 to 0
    in "L2" -> 0 to -1
    in "R0" -> 0 to 1
    else -> throw AssertionError()
}

fun parse(inp: String, createInstruction: (String, String, String) -> Instruction): List<Instruction> {
    val pattern = Regex("""(\w) (\d+) \(#(\w{6})\)""")
    return pattern.findAll(inp).map {
        val (dir, step, color) = it.groupValues.drop(1)
        createInstruction(dir, step, color)
    }.toList()
}

fun compute(instructions: List<Instruction>): Long {
    var currentPosition = 0L to 0L
    val points = mutableListOf<Position>()
    var borderPoints = 0

    for (instruction in instructions) {
        points.add(currentPosition)
        borderPoints += instruction.steps
        currentPosition = currentPosition.let { (y, x) ->
            y + instruction.direction.first * instruction.steps to x + instruction.direction.second * instruction.steps
        }
    }

    // https://en.wikipedia.org/wiki/Shoelace_formula
    val area = abs(points.mapIndexed { i, point ->
        point.first * (points[(i + 1) % points.size].second - points.getOrElse(i - 1) { points.last() }.second)
    }.sum()) / 2

    // https://en.wikipedia.org/wiki/Pick%27s_theorem
    val interiorArea = area - borderPoints / 2 + 1

    return interiorArea + borderPoints
}

fun first(inp: String): Long {
    val instructions = parse(inp) { dir, step, _ ->
        Instruction(dir.asDirection(), step.toInt())
    }

    return compute(instructions)
}

fun second(inp: String): Long {
    val instructions = parse(inp) { _, _, color ->
        Instruction(color.takeLast(1).asDirection(), color.dropLast(1).toInt(16))
    }

    return compute(instructions)
}

fun main() {
    val testInput = File("src/day18/test-input.txt").readText()
    val input = File("src/day18/input.txt").readText()
    println(first(input))
    println(second(input))
}
