import java.io.File
import kotlin.math.pow

val testInput = """
    Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
    Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
    Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
    Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
    Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
""".trimIndent()

val input = File("input.txt").readText()

private data class Card(val number: Int, val winningNumbers: Set<Int>, val myNumbers: Set<Int>)

private fun parse(inp: String): List<Card> {
    val numberPattern = Regex("""\d+""")

    return inp.lineSequence().map { line ->
        val (cardStr, numStr) = line.split(":")
        val (winningNumbersStr, myNumbersStr) = numStr.split("|")

        Card(
            cardStr.split(" ").last().toInt(),
            numberPattern.findAll(winningNumbersStr).map { it.value.toInt() }.toSet(),
            numberPattern.findAll(myNumbersStr).map { it.value.toInt() }.toSet(),
        )
    }.toList()
}

private fun first(inp: String): Int {
    return parse(inp).sumOf { card ->
        (2.0).pow(card.winningNumbers.intersect(card.myNumbers).size - 1).toInt()
    }
}

private fun numberOfScratchCards(cards: List<Card>, index: Int): Int {
    val card = cards[index]
    val matchingNumbers = card.winningNumbers.intersect(card.myNumbers).size

    return 1 + (index + 1..index + matchingNumbers).sumOf { numberOfScratchCards(cards, it) }
}

private fun second(inp: String): Int {
    val cards = parse(inp)

    return cards.indices.sumOf { numberOfScratchCards(cards, it) }
}

println(first(input))
println(second(input))
