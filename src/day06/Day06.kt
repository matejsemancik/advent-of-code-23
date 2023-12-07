package day06

import findIntegers
import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val (times, distances) = input.map { line -> line.findIntegers() }
        val races = times.indices.map { index ->
            times[index] to distances[index]
        }

        return races.map { (timeLimit, distanceRecord) ->
            (0..timeLimit).map {
                // Calculate distance traveled for each variation
                (timeLimit - it) * it
            }.count { distanceTraveled -> distanceTraveled > distanceRecord }
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val (timeLimit, distanceRecord) = input.map { line -> line.filter { it.isDigit() }.toLong() }

        return (0..timeLimit).map {
            (timeLimit - it) * it
        }.count { distanceTraveled -> distanceTraveled > distanceRecord }
    }

    val testInput = readInput("day06/test")
    val input = readInput("day06/input")

    check(part1(testInput) == 288) { "Check part 1" }
    check(part2(testInput) == 71503) { "Check part 2" }

    part1(input).println()
    part2(input).println()
}
