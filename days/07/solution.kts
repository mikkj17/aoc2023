import java.io.File

val testInput = """
    32T3K 765
    T55J5 684
    KK677 28
    KTJJT 220
    QQQJA 483
""".trimIndent()

val input = File("input.txt").readText()

private enum class Type {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_KIND,
    FULL_HOUSE,
    FOUR_KIND,
    FIVE_KIND,
}

private data class Hand(val cards: String, val bid: Int, val jokers: Boolean) : Comparable<Hand> {
    val labels = (if (!jokers) "AKQJT98765432" else "AKQT98765432J").reversed()
    val type = if (!jokers) getType(cards) else labels.maxOf { getType(cards.replace("J", it.toString())) }

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
        if (this.type != other.type) {
            return this.type.compareTo(other.type)
        }

        for ((thisCard, otherCard) in this.cards.zip(other.cards)) {
            if (thisCard != otherCard) {
                return labels.indexOf(thisCard).compareTo(labels.indexOf(otherCard))
            }
        }

        throw AssertionError("Should not get to this point...")
    }
}

private fun parse(inp: String, jokers: Boolean): List<Hand> {
    return inp.lines().map { line ->
        val (cards, bid) = line.split(" ")
        Hand(cards, bid.toInt(), jokers)
    }
}

private fun compute(inp: String, jokers: Boolean): Int {
    return parse(inp, jokers)
        .sorted()
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()
}

private fun first(inp: String): Int = compute(inp, false)

private fun second(inp: String): Int = compute(inp, true)

println(first(input))
println(second(input))
