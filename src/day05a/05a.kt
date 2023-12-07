package day05a

import java.io.File

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

fun main() {
    val input = File("inputs/05a.txt")
    val text = input.readText()
    val lines = input.readLines()

    val numberRegex = """\d+""".toRegex()

    val seeds = numberRegex.findAll(lines[0]).map(MatchResult::value).map(String::toLong).toList()

    val maps = text.split("\n\n")

    val seedToSoilMap = maps[1].split('\n').skipFirst().map(String::toMapObject)
    val soilToFertilizerMap = maps[2].split('\n').skipFirst().map(String::toMapObject)
    val fertilizerToWaterMap = maps[3].split('\n').skipFirst().map(String::toMapObject)
    val waterToLightMap = maps[4].split('\n').skipFirst().map(String::toMapObject)
    val lightToTemperatureMap = maps[5].split('\n').skipFirst().map(String::toMapObject)
    val temperatureToHumidityMap = maps[6].split('\n').skipFirst().map(String::toMapObject)
    val humidityToLocationMap = maps[7].split('\n').skipFirst().skipLast().map(String::toMapObject)

    val soils = seeds.map { s -> seedToSoilMap.firstOrNull { it.getSourceRange().contains(s) }?.getDestinationForSource(s) ?: s }
    val fertilizer = soils.map { s -> soilToFertilizerMap.firstOrNull { it.getSourceRange().contains(s) }?.getDestinationForSource(s) ?: s }
    val water = fertilizer.map { f -> fertilizerToWaterMap.firstOrNull { it.getSourceRange().contains(f) }?.getDestinationForSource(f) ?: f }
    val light = water.map { w -> waterToLightMap.firstOrNull { it.getSourceRange().contains(w) }?.getDestinationForSource(w) ?: w }
    val temperature = light.map { l -> lightToTemperatureMap.firstOrNull { it.getSourceRange().contains(l) }?.getDestinationForSource(l) ?: l }
    val humidity = temperature.map { t -> temperatureToHumidityMap.firstOrNull { it.getSourceRange().contains(t) }?.getDestinationForSource(t) ?: t }
    val location = humidity.map { h -> humidityToLocationMap.firstOrNull { it.getSourceRange().contains(h) }?.getDestinationForSource(h) ?: h }

    println("Min Location: ${location.min()}")
}

private fun List<String>.skipFirst(): List<String> = this.subList(1, this.size)
private fun List<String>.skipLast(): List<String> = this.subList(0, this.size - 1)

private fun String.toMapObject(): MapObject {
    val split = this.split(' ')

    return MapObject(split[0].toLong(), split[1].toLong(), split[2].toLong())
}