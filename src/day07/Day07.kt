package day07

import println
import readInput

private val Parser = "(.{5}) (\\d+)".toRegex()
private const val Labels = "23456789TJQKA"
private const val JokerAwareLabels = "J23456789TQKA"

@JvmInline
private value class Card(val value: String) {
    enum class Type {
        HighCard,
        OnePair,
        TwoPair,
        ThreeOfAKind,
        FullHouse,
        FourOfAKind,
        FiveOfAKind,
    }
}

private fun Card.labelFrequencies(): Map<Char, Int> = value.toCharArray().groupBy { it }.mapValues { it.value.count() }
private fun Card.labelCounts(): List<Int> = labelFrequencies().values.sortedDescending()

private val Card.type: Card.Type
    get() = when {
        labelCounts() == listOf(5) -> Card.Type.FiveOfAKind
        labelCounts() == listOf(4, 1) -> Card.Type.FourOfAKind
        labelCounts() == listOf(3, 2) -> Card.Type.FullHouse
        labelCounts() == listOf(3, 1, 1) -> Card.Type.ThreeOfAKind
        labelCounts() == listOf(2, 2, 1) -> Card.Type.TwoPair
        labelCounts() == listOf(2, 1, 1, 1) -> Card.Type.OnePair
        else -> Card.Type.HighCard
    }

private val Card.jokerAwareType: Card.Type
    get() {
        if (!value.contains('J')) {
            return type
        }

        if (value.all { it == 'J' }) {
            return Card.Type.FiveOfAKind
        }

        // Convert all `J`s to label with the highest frequency on current hand
        val charWithHighestFrequency = labelFrequencies().toList()
            .sortedByDescending { (_, labelCount) -> labelCount }
            .filterNot { (char, _) -> char == 'J' }
            .first().first

        return Card(value = value.replace('J', charWithHighestFrequency)).type
    }

private fun typeComparator(cardType: (Card) -> Card.Type): Comparator<Pair<Card, Int>> =
    compareBy { cardType(it.first) }

private fun tieComparator(priorityMask: String): Comparator<Pair<Card, Int>> = Comparator { o1, o2 ->
    val o1Chars = o1.first.value.toCharArray().map { priorityMask.indexOf(it) }
    val o2Chars = o2.first.value.toCharArray().map { priorityMask.indexOf(it) }
    for (i in o1Chars.indices) {
        if (o1Chars[i].compareTo(o2Chars[i]) == 0) {
            continue
        }
        return@Comparator o1Chars[i].compareTo(o2Chars[i])
    }
    return@Comparator 0
}

fun main() {
    fun part1(input: List<String>): Int {
        val cardsToBids = input.map { line ->
            Parser.find(line)?.let { Card(it.groupValues[1]) to it.groupValues[2].toInt() }
                ?: error("Cannot parse input")
        }

        return cardsToBids
            .sortedWith(typeComparator { card -> card.type }.then(tieComparator(Labels)))
            .mapIndexed { index, pair -> (index + 1) * pair.second }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cardsToBids = input.map { line ->
            Parser.find(line)?.let { Card(it.groupValues[1]) to it.groupValues[2].toInt() }
                ?: error("Cannot parse input")
        }

        return cardsToBids
            .sortedWith(typeComparator { card -> card.jokerAwareType }.then(tieComparator(JokerAwareLabels)))
            .mapIndexed { index, pair -> (index + 1) * pair.second }
            .sum()
    }

    val testInput = readInput("day07/test")
    val input = readInput("day07/input")

    check(part1(testInput) == 6440) { "Check part 1" }
    check(part2(testInput) == 5905) { "Check part 2" }

    part1(input).println()
    part2(input).println()
}
