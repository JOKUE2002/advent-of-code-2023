package day05b

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.atomic.AtomicLong

private data class MapObject (
    val destinationStart: Long,
    val sourceStart: Long,
    val length: Long
) {
    fun getSourceRange(): LongRange = (sourceStart..<(sourceStart + length))

    /**
     * null if [source] not in sourceRange (see [getSourceRange])
     */
    fun getDestinationForSource(source: Long): Long? {
        if (!getSourceRange().contains(source))
            return null

        return destinationStart + (source - sourceStart)
    }
}

suspend fun main() {
    val input = File("inputs/05a.txt")
    val text = input.readText()
    val lines = input.readLines()

    val numberRegex = """\d+""".toRegex()

    val seedInput = numberRegex.findAll(lines[0]).map(MatchResult::value).map(String::toLong).toList()

    val seedRanges = seedInput.chunked(2).map { (start, length) -> start..<(start + length) }

    val maps = text.split("\n\n")

    val seedToSoilMap = maps[1].split('\n').skipFirst().map(String::toMapObject)
    val soilToFertilizerMap = maps[2].split('\n').skipFirst().map(String::toMapObject)
    val fertilizerToWaterMap = maps[3].split('\n').skipFirst().map(String::toMapObject)
    val waterToLightMap = maps[4].split('\n').skipFirst().map(String::toMapObject)
    val lightToTemperatureMap = maps[5].split('\n').skipFirst().map(String::toMapObject)
    val temperatureToHumidityMap = maps[6].split('\n').skipFirst().map(String::toMapObject)
    val humidityToLocationMap = maps[7].split('\n').skipFirst().skipLast().map(String::toMapObject)

    val finalLocation = AtomicLong(Long.MAX_VALUE)
    runBlocking {
        seedRanges.map { range ->
            async(Dispatchers.Default) {
                range.forEach { seed ->
                    val soil =
                        seedToSoilMap.firstOrNull { it.getSourceRange().contains(seed) }?.getDestinationForSource(seed)
                            ?: seed
                    val fertilizer = soilToFertilizerMap.firstOrNull { it.getSourceRange().contains(soil) }
                        ?.getDestinationForSource(soil) ?: soil
                    val water = fertilizerToWaterMap.firstOrNull { it.getSourceRange().contains(fertilizer) }
                        ?.getDestinationForSource(fertilizer) ?: fertilizer
                    val light = waterToLightMap.firstOrNull { it.getSourceRange().contains(water) }
                        ?.getDestinationForSource(water) ?: water
                    val temperature = lightToTemperatureMap.firstOrNull { it.getSourceRange().contains(light) }
                        ?.getDestinationForSource(light) ?: light
                    val humidity = temperatureToHumidityMap.firstOrNull { it.getSourceRange().contains(temperature) }
                        ?.getDestinationForSource(temperature) ?: temperature
                    val location = humidityToLocationMap.firstOrNull { it.getSourceRange().contains(humidity) }
                        ?.getDestinationForSource(humidity) ?: humidity

                    if (location < finalLocation.get())
                        finalLocation.set(location)
                }
            }
        }
    }.awaitAll()

    println("Min Location: ${finalLocation.get()}")
}

private fun List<String>.skipFirst(): List<String> = this.subList(1, this.size)
private fun List<String>.skipLast(): List<String> = this.subList(0, this.size - 1)

private fun String.toMapObject(): MapObject {
    val split = this.split(' ')

    return MapObject(split[0].toLong(), split[1].toLong(), split[2].toLong())
}