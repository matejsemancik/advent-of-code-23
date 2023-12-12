package day11

import Coords
import pairs
import println
import readInput
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@JvmInline
value class Cluster(val data: List<String>) {

    val width: Int get() = data[0].length

    val height: Int get() = data.size

    companion object {
        fun parse(input: List<String>) = Cluster(input)
    }

    override fun toString(): String = data.joinToString(separator = "\n") { line -> line }

    /**
     * Returns list of coordinates of Galaxies.
     */
    fun galaxies(): List<Coords> = (0 until height).flatMap { y ->
        (0 until width).mapNotNull { x ->
            if (data[y][x] == '#') {
                Coords(x, y)
            } else {
                null
            }
        }
    }

    fun emptyRows(): Set<Int> = data
        .mapIndexed { index, s -> if (s.all { it == '.' }) index else null }
        .filterNotNull()
        .toSet()

    fun emptyColumns(): Set<Int> = (0 until width)
        .mapNotNull { index ->
            if (data.map { line -> line[index] }.all { it == '.' }) index else null
        }
        .toSet()
}

private fun Pair<Coords, Coords>.shortestPath(
    emptyColumns: Set<Int>,
    emptyRows: Set<Int>,
    expansionMultiplier: Int
): Long {
    val horizontalExpansions = emptyColumns.count { x -> x in (min(first.x, second.x).. max(first.x, second.x)) }
    val verticalExpansions = emptyRows.count { x -> x in (min(first.y, second.y).. max(first.y, second.y)) }

    val horizontalDistance: Long =
        (second.x - first.x).absoluteValue + (horizontalExpansions * (expansionMultiplier.toLong() - 1))
    val verticalDistance: Long =
        (second.y - first.y).absoluteValue + (verticalExpansions * (expansionMultiplier.toLong() - 1))

    return horizontalDistance + verticalDistance
}

fun main() {

    fun calculate(input: List<String>, expansionMultiplier: Int = 2): Long {
        val cluster = Cluster.parse(input)
        val emptyColumns = cluster.emptyColumns()
        val emptyRows = cluster.emptyRows()

        cluster.println()
        return cluster
            .galaxies()
            .pairs()
            .sumOf { pair -> pair.shortestPath(emptyColumns, emptyRows, expansionMultiplier) }
            .also { it.println() }
    }

    val testInput = readInput("day11/test")
    val input = readInput("day11/input")

    check(calculate(testInput) == 374L) { "Multiplier 2" }
    check(calculate(testInput, expansionMultiplier = 10) == 1030L) { "Multiplier 10" }
    check(calculate(testInput, expansionMultiplier = 100) == 8410L) { "Multiplier 100" }

    calculate(input = input)
    calculate(input = input, expansionMultiplier = 1_000_000)
}