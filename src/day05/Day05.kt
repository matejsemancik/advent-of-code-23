package day05

import findLongs
import println
import readInput
import kotlin.time.measureTime

private data class Mapping(
    val srcRange: LongRange,
    val destRange: LongRange
)

private fun String.toMapping(): Mapping {
    val (dest, src, size) = findLongs()
    return Mapping(
        srcRange = src..<src + size,
        destRange = dest..<dest + size
    )
}

private fun List<String>.toMappings(): List<Mapping> = map { it.toMapping() }

private fun Long.remap(mappings: List<Mapping>): Long {
    val mapping = mappings.singleOrNull { mapping -> this in mapping.srcRange } ?: return this

    val indexInSrcRange = (this - mapping.srcRange.first).toInt()
    return mapping.destRange.first + indexInSrcRange
}

private data class Almanac(
    val seedToSoil: List<Mapping>,
    val soilToFertilizer: List<Mapping>,
    val fertilizerToWater: List<Mapping>,
    val waterToLight: List<Mapping>,
    val lightToTemperature: List<Mapping>,
    val temperatureToHumidity: List<Mapping>,
    val humidityToLocation: List<Mapping>,
)

fun main() {

    fun parse(input: List<String>): Pair<List<Long>, Almanac> {
        val seeds = mutableListOf<Long>()
        val seedToSoil = mutableListOf<Mapping>()
        val soilToFertilizer = mutableListOf<Mapping>()
        val fertilizerToWater = mutableListOf<Mapping>()
        val waterToLight = mutableListOf<Mapping>()
        val lightToTemperature = mutableListOf<Mapping>()
        val temperatureToHumidity = mutableListOf<Mapping>()
        val humidityToLocation = mutableListOf<Mapping>()
        var targetList: MutableList<Mapping>? = null

        input
            .forEach {
                when {
                    it.contains("seeds:") -> seeds.addAll(it.findLongs())
                    it.contains("seed-to-soil map:") -> targetList = seedToSoil
                    it.contains("soil-to-fertilizer map:") -> targetList = soilToFertilizer
                    it.contains("fertilizer-to-water map:") -> targetList = fertilizerToWater
                    it.contains("water-to-light map") -> targetList = waterToLight
                    it.contains("light-to-temperature map:") -> targetList = lightToTemperature
                    it.contains("temperature-to-humidity map:") -> targetList = temperatureToHumidity
                    it.contains("humidity-to-location map:") -> targetList = humidityToLocation
                    it.isBlank() -> return@forEach
                    targetList != null -> {
                        targetList?.add(it.toMapping())
                    }
                }
            }

        return seeds.toList() to Almanac(
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        )
    }

    fun part1(input: List<String>): Long {
        val (seeds, almanac) = parse(input)

        println(seeds)
        return seeds.toList().minOf { seed ->
            with(almanac) {
                seed
                    .remap(seedToSoil)
                    .remap(soilToFertilizer)
                    .remap(fertilizerToWater)
                    .remap(waterToLight)
                    .remap(lightToTemperature)
                    .remap(temperatureToHumidity)
                    .remap(humidityToLocation)
                    .also { "Calculated seed: $seed; result: $it".println() }
            }
        }
    }

    fun part2(input: List<String>): Long {
        val (seedPairs, almanac) = parse(input)
        check(seedPairs.count() % 2 == 0) { "Seed ranges must be in pairs" }

        val seedRanges = buildList {
            val queue = ArrayDeque(seedPairs)
            while (queue.isNotEmpty()) {
                add(queue.first() until queue.removeFirst() + queue.removeFirst())
            }
        }

        seedRanges.println()
        return seedRanges.minOf { seedRange ->
            seedRange
                .minOf { seed ->
                    with(almanac) {
                        seed
                            .remap(seedToSoil)
                            .remap(soilToFertilizer)
                            .remap(fertilizerToWater)
                            .remap(waterToLight)
                            .remap(lightToTemperature)
                            .remap(temperatureToHumidity)
                            .remap(humidityToLocation)
                    }
                }.also { "Calculated seed range: $seedRange; result: $it".println() }
        }
    }

    val testInput = readInput("day05/test")
    val input = readInput("day05/input")

    measureTime {
        check(part1(testInput) == 35L) { "Check part 1" }
    }.also { "Took ${it.inWholeSeconds} seconds".println() }

    measureTime {
        check(part2(testInput) == 46L) { "Check part 2" }
    }

    measureTime {
        part1(input).println()
    }.also { "Took ${it.inWholeSeconds} seconds".println() }

    measureTime {
        part2(input).println()
    }.also { "Took ${it.inWholeSeconds} seconds".println() }
}
