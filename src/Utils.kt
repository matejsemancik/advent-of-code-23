import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max

private val RegexNumber = "\\d+".toRegex()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Finds all integers in String.
 */
fun String.findIntegers(): List<Int> = RegexNumber.findAll(this).map { it.value.toInt() }.toList()

fun String.findLongs(): List<Long> = RegexNumber.findAll(this).map { it.value.toLong() }.toList()

/**
 * Splits String by new line delimiter.
 */
fun String.splitByNewLine(): List<String> = split("\n")

/**
 * Returns first matching group for provided [input].
 */
fun Regex.firstGroup(input: CharSequence) = find(input)?.groupValues?.get(1) ?: error("Could not match provided input")

/**
 * Finds least common multiple between two numbers.
 */
fun lcm(a: Long, b: Long): Long {
    val larger = max(a, b)
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }

    return maxLcm
}

/**
 * Finds least common multiple between all numbers in this [Collection].
 */
fun Collection<Long>.lcm() = reduce { acc, l -> lcm(acc, l) }
