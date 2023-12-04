package day04

import println
import readInput
import kotlin.math.pow

private val RegexCard = "Card (\\d+)".toRegex()
private val RegexNumber = "\\d+".toRegex()

private data class Card(
    val number: Int, val winningNumbers: Set<Int>, val scratchedNumbers: Set<Int>
) {
    companion object {
        fun parse(input: String): Card {
            val (card, numbers) = input.split(":")
            val (winningNumbers, myNumbers) = numbers.split("|").let { (win, my) ->
                    RegexNumber.findAll(win).map { it.value.toInt() }.toSet() to RegexNumber.findAll(my)
                        .map { it.value.toInt() }.toSet()
                }

            return Card(
                number = RegexCard.find(input)?.groupValues?.get(1)?.toInt() ?: 0,
                winningNumbers = winningNumbers,
                scratchedNumbers = myNumbers
            )
        }
    }
}

private fun Card.points() = winningNumbers.intersect(scratchedNumbers).count()

fun main() {

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach { line ->
            val card = Card.parse(line)
            if (card.points() > 0) {
                sum += 2.0.pow(card.points() - 1).toInt()
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { line -> Card.parse(line) }
        val stacksOfCards = cards.map { mutableListOf(it) }

        for (cards in stacksOfCards) {
            for (card in cards) {
                val points = card.points()
                if (points > 0) {
                    for (p in 1..points) {
                        stacksOfCards[stacksOfCards.indexOf(cards) + p].add(stacksOfCards[stacksOfCards.indexOf(cards) + p][0])
                    }
                }
            }
        }

        return stacksOfCards.sumOf { stack -> stack.count() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/test")
    check(part1(testInput) == 13) { "Check part 1" }
    check(part2(testInput) == 30) { "Check part 2" }

    val input = readInput("day04/input")
    part1(input).println()
    part2(input).println()
}
