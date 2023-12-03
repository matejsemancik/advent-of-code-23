package day02

import println
import readInput

private data class Game(
    val id: Int,
    val sets: List<GameSet>
) {
    companion object {

        private val RegexGame = "Game (\\d+)".toRegex()
        private val RegexRed = "(\\d+) red".toRegex()
        private val RegexGreen = "(\\d+) green".toRegex()
        private val RegexBlue = "(\\d+) blue".toRegex()

        fun parse(input: String): Game {
            val (gameString, setsString) = input.split(':')

            return Game(
                id = RegexGame.find(gameString)?.groupValues?.get(1)?.toInt() ?: error("Game ID could not be parsed"),
                sets = setsString
                    .split(';')
                    .map { setInput ->
                        val r = RegexRed.find(setInput)?.groupValues?.get(1)?.toInt() ?: 0
                        val g = RegexGreen.find(setInput)?.groupValues?.get(1)?.toInt() ?: 0
                        val b = RegexBlue.find(setInput)?.groupValues?.get(1)?.toInt() ?: 0
                        GameSet(r, g, b)
                    }
            )
        }
    }

    data class GameSet(
        val red: Int,
        val green: Int,
        val blue: Int
    )
}

private const val RedCap = 12
private const val GreenCap = 13
private const val BlueCap = 14

private fun Game.maxOfRed(): Int = sets.maxOf { it.red }
private fun Game.maxOfGreen() = sets.maxOf { it.green }
private fun Game.maxOfBlue() = sets.maxOf { it.blue }

private fun Game.power() = maxOfRed() * maxOfGreen() * maxOfBlue()

fun main() {
    fun part1(input: List<String>): Int {
        val games = input.map { line -> Game.parse(line) }
        val possibleGames = games
            .filter { game ->
                game.sets.all { set ->
                    set.red <= RedCap && set.green <= GreenCap && set.blue <= BlueCap
                }
            }

        return possibleGames.sumOf { game -> game.id }
    }

    fun part2(input: List<String>): Int {
        val games = input.map { line -> Game.parse(line) }
        return games.map { game -> game.power() }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/test")
    check(part1(testInput) == 8) { "Check part 1" }
    check(part2(testInput) == 2286) { "check part 2" }

    val input = readInput("day02/input")
    part1(input).println()
    part2(input).println()
}
