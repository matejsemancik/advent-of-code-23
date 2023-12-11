package day10

import println
import readInput
import test

@JvmInline
private value class Map(val array: Array<CharArray>) {

    companion object {
        fun parse(input: List<String>): Map {
            val height = input.size
            val width = input[0].length
            val startY = input.indexOfFirst { it.contains('S') }
            val startX = input[startY].indexOfFirst { it == 'S' }
            val array = Array(height) { CharArray(width) { '.' } }
            val characters = mutableListOf('S')
            array[startY][startX] = 'S'

            var y = startY
            var x = startX

            // Find initial direction and move
            var direction: Direction = input[y][x].directions().first { dir ->
                val nextY = (y + dir.vector[0])
                val nextX = (x + dir.vector[1])
                if (nextY !in 0..<height) {
                    return@first false
                }
                if (nextX !in 0..<width) {
                    return@first false
                }
                val nextChar = input[nextY][nextX]
                return@first nextChar.isConnectableFrom(dir)
            }
            y += direction.vector[0]
            x += direction.vector[1]

            while (y != startY || x != startX) {
                array[y][x] = input[y][x]
                characters += input[y][x]
                val newDirection = input[y][x].directions().filterNot { it == direction.opposite() }.single()
                direction = newDirection
                y += direction.vector[0]
                x += direction.vector[1]
            }

            return Map(array).apply { fillLoop('*') }
        }
    }

    val width: Int
        get() = array[0].size

    val height: Int
        get() = array.size

    override fun toString(): String {
        val trace = "\u001B[32m"
        val reset = "\u001b[0m"
        val bitmap = array.joinToString(separator = "\n") {
            it.joinToString(separator = "") { char ->
                when (char) {
                    'S' -> "${trace}S$reset"
                    '|' -> "$trace│$reset"
                    '-' -> "$trace─$reset"
                    'L' -> "$trace└$reset"
                    'J' -> "$trace┘$reset"
                    '7' -> "$trace┐$reset"
                    'F' -> "$trace┌$reset"
                    else -> char.toString()
                }
            }
        }

        return "\n$bitmap\nlength: ${loopLength()}\nhalf length: ${loopLength() / 2}\nfill: ${fillCount()}"
    }
}

private val PipeRegex = "[S|\\-LJ7F]".toRegex()
private val BoundaryRegex = "(F-*J)|(L-*7)|(\\|)".toRegex()

private fun Map.loopLength(): Int = array.sumOf { it.count { it.toString().matches(PipeRegex) } }
private fun Map.fillCount(): Int = array.sumOf { it.count { it == '*' } }
private fun Map.fillLoop(char: Char) {
    for (y in 0..<height) {
        for (x in 0..<width) {
            if (array[y][x] == '.') {
                // Cast a horizontal ray and count transitions between nodes
                val boundaries = BoundaryRegex.findAll(array[y].drop(x).joinToString(separator = "")).count()
                array[y][x] = if (boundaries % 2 == 0) {
                    '.'
                } else {
                    char
                }
            }
        }
    }
}

private enum class Direction(val vector: IntArray) {
    Up(intArrayOf(-1, 0)),
    Down(intArrayOf(1, 0)),
    Left(intArrayOf(0, -1)),
    Right(intArrayOf(0, 1))
}

private fun Direction.opposite(): Direction = when (this) {
    Direction.Up -> Direction.Down
    Direction.Down -> Direction.Up
    Direction.Left -> Direction.Right
    Direction.Right -> Direction.Left
}

private fun Char.directions(): List<Direction> {
    return when (this) {
        'S' -> listOf(Direction.Right, Direction.Up, Direction.Left, Direction.Down)
        '|' -> listOf(Direction.Up, Direction.Down)
        '-' -> listOf(Direction.Left, Direction.Right)
        'L' -> listOf(Direction.Up, Direction.Right)
        'J' -> listOf(Direction.Left, Direction.Up)
        '7' -> listOf(Direction.Left, Direction.Down)
        'F' -> listOf(Direction.Down, Direction.Right)
        else -> emptyList()
    }
}

private fun Char.oppositeDirections(): Array<Direction> = directions().map { it.opposite() }.toTypedArray()

private fun Char.isConnectableFrom(direction: Direction) = oppositeDirections().contains(direction)

fun main() {

    fun part1(input: List<String>): Int {
        val map = Map.parse(input)
        map.println()
        return map.loopLength() / 2
    }

    fun part2(input: List<String>): Int {
        val map = Map.parse(input)
        map.println()
        return map.fillCount()
    }

    test(name = "Part1", expected = 8) { part1(readInput("day10/test_part1")) }
    test(name = "Part2 0", expected = 4) { part2(readInput("day10/test_part2_0")) }
    test(name = "Part2 1", expected = 4) { part2(readInput("day10/test_part2_1")) }
    test(name = "Part2 2", expected = 8) { part2(readInput("day10/test_part2_2")) }
    test(name = "Part2 3", expected = 10) { part2(readInput("day10/test_part2_3")) }
    test(name = "Part2 4", expected = 6) { part2(readInput("day10/test_part2_4")) }

    part1(readInput("day10/input")).println()
    part2(readInput("day10/input")).println()
}