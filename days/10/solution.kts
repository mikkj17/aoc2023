import Solution.Position
import java.io.File

typealias Position = Pair<Int, Int>

data class Grid(val graph: Map<Position, Node>) {
    val start = graph.values.first { it.isStart }
}

data class Node(val pipe: Char, val pos: Position) {
    val isStart get() = pipe == 'S'

    fun canMove(node: Node): Boolean = pos in node.connections()

    fun connections(): Set<Position> {
        val (y, x) = pos

        return when (pipe) {
            '|' -> setOf(y - 1 to x, y + 1 to x)
            '-' -> setOf(y to x - 1, y to x + 1)
            'L' -> setOf(y - 1 to x, y to x + 1)
            'J' -> setOf(y - 1 to x, y to x - 1)
            '7' -> setOf(y + 1 to x, y to x - 1)
            'F' -> setOf(y + 1 to x, y to x + 1)
            '.' -> emptySet()
            'S' -> setOf(
                y to x - 1,
                y - 1 to x,
                y to x + 1,
                y + 1 to x
            )

            else -> throw AssertionError("Unknown pipe: $pipe")
        }
    }
}

fun parse(inp: String): Grid {
    val grid = mutableMapOf<Position, Node>()

    inp.lineSequence().forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            val pos = y to x
            grid[pos] = Node(char, pos)
        }
    }

    return Grid(grid)
}

fun depthFirstSearch(grid: Grid): Set<Node> {
    val queue = ArrayDeque(listOf(grid.start))
    val discovered = mutableSetOf<Node>()

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        if (!discovered.contains(node)) {
            discovered.add(node)
            for (pos in node.connections()) {
                val neighBor = grid.graph[pos] ?: continue

                if (node.canMove(neighBor)) {
                    queue.add(neighBor)
                }
            }
        }
    }

    return discovered
}

fun first(inp: String): Int {
    val grid = parse(inp)
    val circle = depthFirstSearch(grid)

    return circle.size / 2
}

fun second(inp: String): Int {
    val grid = parse(inp)
    val circle = depthFirstSearch(grid)
    val circlePositions = circle.map { it.pos }.toSet()

    var numberOfEnclosed = 0
    inp.lineSequence().forEachIndexed { y, row ->
        val inRow = circle.filter { it.pos.first == y }

        for (x in row.indices) {
            if (circlePositions.contains(y to x)) {
                continue
            }

            // TODO: I think this only works because my "S" is a "7"
            val enclosed = inRow.filter { it.pos.second < x }.count { it.pipe in "SF7|" } % 2 == 1
            if (enclosed) {
                numberOfEnclosed++
            }

        }
    }

    return numberOfEnclosed
}

val testInput = File("test-input.txt").readText()
val input = File("input.txt").readText()
println(first(input))
println(second(input))
