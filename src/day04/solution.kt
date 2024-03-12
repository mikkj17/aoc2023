package day04

import java.io.File
import kotlin.math.pow

data class Card(val number: Int, val winningNumbers: Set<Int>, val myNumbers: Set<Int>)

fun parse(inp: String): List<Card> {
    val numberPattern = Regex("""\d+""")

    return inp.lines().map { line ->
        val (cardStr, numStr) = line.split(":")
        val (winningNumbersStr, myNumbersStr) = numStr.split("|")

        Card(
            cardStr.split(" ").last().toInt(),
            numberPattern.findAll(winningNumbersStr).map { it.value.toInt() }.toSet(),
            numberPattern.findAll(myNumbersStr).map { it.value.toInt() }.toSet(),
        )
    }
}

fun first(inp: String): Int {
    return parse(inp).sumOf { card ->
        (2.0).pow(card.winningNumbers.intersect(card.myNumbers).size - 1).toInt()
    }
}

fun numberOfScratchCards(cards: List<Card>, index: Int): Int {
    val card = cards[index]
    val matchingNumbers = card.winningNumbers.intersect(card.myNumbers).size

    return 1 + (index + 1..index + matchingNumbers).sumOf { numberOfScratchCards(cards, it) }
}

fun second(inp: String): Int {
    val cards = parse(inp)

    return cards.indices.sumOf { numberOfScratchCards(cards, it) }
}

fun main() {
    val testInput = File("src/day04/test-input.txt").readText()
    val input = File("src/day04/input.txt").readText()
    println(first(input))
    println(second(input))
}
