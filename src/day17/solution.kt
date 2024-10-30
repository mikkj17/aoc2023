package day17

import java.io.File
import java.util.*

typealias Position = Pair<Int, Int>

typealias Direction = Pair<Int, Int>

data class State(val position: Position, val direction: Direction, val repeats: Int)

fun parse(inp: String): List<List<Int>> {
    return inp.lines().map { line -> line.map { it.digitToInt() } }
}

fun step(position: Position, direction: Direction) = position.let { (y, x) ->
    y + direction.first to x + direction.second
}

fun getHeatLoss(grid: List<List<Int>>, position: Position) = grid
    .getOrNull(position.first)
    ?.getOrNull(position.second)

fun search(grid: List<List<Int>>, canMove: (State, Direction) -> Boolean, canStop: (State) -> Boolean): Int {
    val comparator: Comparator<Pair<Int, State>> = compareBy { it.first }
    val queue = PriorityQueue(comparator)
    queue.add(0 to State(0 to 0, 0 to 1, 0))
    queue.add(0 to State(0 to 0, 1 to 0, 0))
    val expanded = mutableSetOf<State>()

    while (queue.isNotEmpty()) {
        val (heatLoss, state) = queue.poll()
        if (state in expanded) {
            continue
        }

        if (state.position == grid.size - 1 to grid.first().size - 1 && canStop(state)) {
            return heatLoss
        }

        expanded.add(state)

        for (direction in listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)) {
            if (canMove(state, direction)) {
                val nextPosition = step(state.position, direction)
                val newHeatLoss = getHeatLoss(grid, nextPosition) ?: continue
                val repeats = if (direction == state.direction) state.repeats + 1 else 1
                queue.add(heatLoss + newHeatLoss to State(nextPosition, direction, repeats))
            }
        }
    }

    throw AssertionError("shouldn't happen")
}

fun first(inp: String): Int {
    return search(
        grid = parse(inp),
        canMove = { state, direction ->
            when (direction) {
                -state.direction.first to -state.direction.second -> false
                state.direction -> state.repeats < 3
                else -> true
            }
        },
        canStop = { true }
    )
}

fun second(inp: String): Int {
    return search(
        grid = parse(inp),
        canMove = { state, direction ->
            when (direction) {
                -state.direction.first to -state.direction.second -> false
                state.direction -> state.repeats < 10
                else -> state.repeats >= 4
            }
        },
        canStop = { it.repeats >= 4 }
    )
}

fun main() {
    val testInput = File("src/day17/test-input.txt").readText()
    val input = File("src/day17/input.txt").readText()
    println(first(input))
    println(second(input))
}
