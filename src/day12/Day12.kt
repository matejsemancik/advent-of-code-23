package day12

import findIntegers
import println
import readInput

fun main() {

    fun place(pattern: String, configuration: List<Int>): Int {
        val groups = pattern.split("\\.+".toRegex()).filter { it.isNotEmpty() }

        if (pattern.all { it == '.' || it == '#' }) {
            // Pattern is complete, evaluate if matches the configuration
            if (groups.count() != configuration.count()) {
                return 0
            }

            return if (groups.map { it.count() }.zip(configuration).all { (a, b) -> a == b }) {
                pattern.println()
                1
            } else {
                0
            }
        }

        val left = pattern.replaceFirst('?', '.')
        val right = pattern.replaceFirst('?', '#')

        return place(left, configuration) + place(right, configuration)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val split = line.split(' ')
            val pattern = split[0]
            val digits = split[1].findIntegers()

            place(pattern, digits)
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("day12/test")
    val input = readInput("day12/input")

    check(part1(testInput) == 21) { "Part 1" }
    part1(input).println()

    check(part2(testInput) == 525152) { "Part 2" }
    part2(input).println()
}