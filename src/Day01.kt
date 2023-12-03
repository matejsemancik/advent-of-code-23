private val LookupTable = mapOf(
    "one" to "o1e",
    "two" to "t2o",
    "three" to "t3ree",
    "four" to "f4ur",
    "five" to "f5ve",
    "six" to "s6x",
    "seven" to "s7ven",
    "eight" to "e8ght",
    "nine" to "n9ne"
)

private fun extractNumber(line: String): Int? = line
    .filter { it.isDigit() }
    .let { it.firstOrNull()?.plus(it.last().toString()) }?.toInt()

private fun transformLine(line: String): String {
    var transformedLine = line
    LookupTable.forEach { (number, transformation) ->
        transformedLine = transformedLine.replace(number, transformation)
    }
    return transformedLine
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .mapNotNull { extractNumber(it) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .mapNotNull { line ->
                val transformedLine = transformLine(line)
                val lineDigitsOnly = transformedLine.filter { it.isDigit() }
                val number = extractNumber(lineDigitsOnly)
                println("$line -> $transformedLine -> $lineDigitsOnly -> $number")
                return@mapNotNull number
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val part1TestInput = readInput("Day01_part1_test")
    val part2TestInput = readInput("Day01_part2_test")
    check(part1(part1TestInput) == 142) { "Test part1" }
    check(part2(part2TestInput) == 281) { "Test part2" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
