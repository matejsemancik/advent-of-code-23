package day03

import println
import readInput

private val RegexPart = "\\d+".toRegex()
private val RegexSymbol = "[^\\.^\\d]".toRegex()

private data class Position(val x: Int, val y: Int)

private data class Schematic(
    val width: Int,
    val height: Int,
    val rawData: List<String>,
    val parts: List<Part>,
    val symbols: List<Symbol>
) {
    val gears: List<Symbol>
        get() = symbols.filter { it.value == "*" }

    constructor(input: List<String>) : this(
        width = input[0].count(),
        height = input.count(),
        rawData = input,
        parts = input.flatMapIndexed { index, row ->
            RegexPart
                .findAll(row)
                .map { matchResult ->
                    Part(
                        number = matchResult.value.toInt(),
                        positions = matchResult.range.map { x ->
                            Position(x, index)
                        }
                    )
                }
        },
        symbols = input.flatMapIndexed { index, row ->
            RegexSymbol
                .findAll(row)
                .map { matchResult ->
                    Symbol(
                        value = matchResult.value,
                        position = Position(
                            x = matchResult.range.first,
                            y = index
                        )
                    )
                }
        }
    )

    data class Part(
        val number: Int,
        val positions: List<Position>,
    )

    data class Symbol(
        val value: String,
        val position: Position
    )
}

private fun Schematic.Symbol.adjacentPositions(): Set<Position> = buildSet {
    with(this@adjacentPositions.position) {
        add(Position(x = x - 1, y = y - 1))
        add(Position(x = x, y = y - 1))
        add(Position(x = x + 1, y = y - 1))
        add(Position(x = x - 1, y = y))
        add(Position(x = x + 1, y = y))
        add(Position(x = x - 1, y = y + 1))
        add(Position(x = x, y = y + 1))
        add(Position(x = x + 1, y = y + 1))
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val schematic = Schematic(input)

        // Find parts adjacent to each symbol
        val allParts = schematic.parts.toMutableList()
        return schematic.symbols.flatMap { symbol ->
            val validParts = allParts.filter { part ->
                part.positions.intersect(symbol.adjacentPositions().toSet()).isNotEmpty()
            }
            allParts.removeAll(validParts)
            validParts
        }.sumOf { part -> part.number }
    }

    fun part2(input: List<String>): Int {
        val schematic = Schematic(input)

        // Find all gears and ratios
        val allParts = schematic.parts.toMutableList()
        val gearRatios = schematic.gears.mapNotNull { gear ->
            val gearParts = allParts.filter { part ->
                part.positions.intersect(gear.adjacentPositions().toSet()).isNotEmpty()
            }
            return@mapNotNull if (gearParts.count() == 2) {
                gearParts[0].number * gearParts[1].number
            } else {
                null
            }
        }

        return gearRatios.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/test")
    check(part1(testInput) == 4361) { "Check part 1" }
    check(part2(testInput) == 467835) { "Check part 2" }

    val input = readInput("day03/input")
    part1(input).println()
    part2(input).println()
}
