package day08

import lcm
import println
import readInput
import kotlin.time.measureTime

fun main() {

    fun Map<String, Pair<String, String>>.findPath(instructions: String, from: String, to: Regex): Long {
        var currentNode = from
        var stepsTaken = 0L
        while (true) {
            for (instr in instructions) {
                if (currentNode.matches(to)) {
                    return stepsTaken
                }
                currentNode = when (instr) {
                    'L' -> get(currentNode)?.first ?: error("node not found")
                    'R' -> get(currentNode)?.second ?: error("node not found")
                    else -> error("invalid instruction: $instr")
                }
                stepsTaken++
            }
        }
    }

    fun part1(input: List<String>): Long {
        var instructions = ""
        val nodes = mutableMapOf<String, Pair<String, String>>()

        input.forEach { line ->
            when {
                line.isEmpty() -> return@forEach
                line.contains("=") -> "(.+) = \\((.+), (.+)\\)".toRegex().find(line)?.let {
                    nodes.put(it.groupValues[1], it.groupValues[2] to it.groupValues[3])
                }

                else -> instructions = line
            }
        }

        return nodes.findPath(instructions = instructions, from = "AAA", to = "ZZZ".toRegex())
    }

    fun part2(input: List<String>): Long {
        var instructions = ""
        val nodes = mutableMapOf<String, Pair<String, String>>()

        input.forEach { line ->
            when {
                line.isEmpty() -> return@forEach
                line.contains("=") -> "(.+) = \\((.+), (.+)\\)".toRegex().find(line)?.let {
                    nodes.put(it.groupValues[1], it.groupValues[2] to it.groupValues[3])
                }

                else -> instructions = line
            }
        }

        val startingNodes = nodes.keys.filter { it.endsWith("A") }.toMutableList()

        return startingNodes
            .map { start ->
                nodes.findPath(instructions = instructions, from = start, to = "\\w\\wZ".toRegex())
            }
            .lcm()
    }

    val testInput1 = readInput("day08/test_part1_1")
    val testInput2 = readInput("day08/test_part1_2")
    val testInputPart2 = readInput("day08/test_part2")
    val input = readInput("day08/input")

    check(part1(testInput1) == 2L) { "Check part 1:1" }
    check(part1(testInput2) == 6L) { "Check part 1:2" }
    check(part2(testInputPart2) == 6L) { "Check part 2" }

    measureTime {
        part1(input).println()
    }.also { "took ${it.inWholeMilliseconds} ms".println() }

    measureTime {
        part2(input).println()
    }.also { "took ${it.inWholeMilliseconds} ms".println() }
}
