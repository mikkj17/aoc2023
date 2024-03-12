package day07

import java.io.File

enum class Type {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_KIND,
    FULL_HOUSE,
    FOUR_KIND,
    FIVE_KIND,
}

data class Hand(val cards: String, val bid: Int, val jokers: Boolean) : Comparable<Hand> {
    private val labels
        get() = (if (!jokers) "AKQJT98765432" else "AKQT98765432J").reversed()
    private val type: Type
        get() {
            return if (!jokers)
                getType(cards)
            else
                labels.maxOf { getType(cards.replace("J", it.toString())) }
        }

    companion object {
        private fun getType(cards: String): Type {
            val kindFrequency = cards.groupingBy { it }.eachCount()

            return when (kindFrequency.size) {
                1 -> Type.FIVE_KIND
                2 -> if (kindFrequency.containsValue(4)) Type.FOUR_KIND else Type.FULL_HOUSE
                3 -> if (kindFrequency.containsValue(3)) Type.THREE_KIND else Type.TWO_PAIR
                else -> if (kindFrequency.size == 4) Type.ONE_PAIR else Type.HIGH_CARD
            }
        }
    }

    override fun compareTo(other: Hand): Int {
        if (type != other.type) {
            return type.compareTo(other.type)
        }

        for ((card, otherCard) in cards.zip(other.cards)) {
            if (card != otherCard) {
                return labels.indexOf(card).compareTo(labels.indexOf(otherCard))
            }
        }

        throw AssertionError("Identical hands: $cards and ${other.cards}")
    }
}

fun parse(inp: String, jokers: Boolean): List<Hand> {
    return inp.lines().map { line ->
        val (cards, bid) = line.split(" ")
        Hand(cards, bid.toInt(), jokers)
    }
}

fun compute(inp: String, jokers: Boolean): Int {
    return parse(inp, jokers)
        .sorted()
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()
}

fun first(inp: String): Int = compute(inp, false)

fun second(inp: String): Int = compute(inp, true)

fun main() {
    val testInput = File("src/day07/test-input.txt").readText()
    val input = File("src/day07/input.txt").readText()
    println(first(input))
    println(second(input))
}
