package day09

import findLongs
import println
import readInput

fun main() {

    fun nextNumberFor(sequence: List<Long>): Long {
        if (sequence.all { it == 0L }) return 0
        val nextSequence = sequence.windowed(2, 1) { (a, b) -> b - a }
        return sequence.last() + nextNumberFor(nextSequence)
    }

    fun previousNumberFor(sequence: List<Long>): Long {
        if (sequence.all { it == 0L }) return 0
        val nextSequence = sequence.windowed(2, 1) { (a, b) -> b - a }
        return sequence.first() - previousNumberFor(nextSequence)
    }

    fun part1(input: List<String>): Long {
        return input.map { line -> line.findLongs() }.sumOf { sequence -> nextNumberFor(sequence) }
    }

    fun part2(input: List<String>): Long {
        return input.map { line -> line.findLongs() }.sumOf { sequence -> previousNumberFor(sequence) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/test")
    val input = readInput("day09/input")
    check(part1(testInput) == 114L) { "Test part1" }
    check(part2(testInput) == 2L) { "Test part2" }

    part1(input).println()
    part2(input).println()
}