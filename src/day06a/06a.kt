package day06a

import java.io.File

fun main() {
    val input = File("inputs/06a.txt")
    val lines = input.readLines()

    val times = Regex("\\d+")
        .findAll(lines[0])
        .map(MatchResult::value)
        .map(String::toInt)
        .toList()

    val distances = Regex("\\d+")
        .findAll(lines[1])
        .map(MatchResult::value)
        .map(String::toInt)
        .toList()


    val winnable = times.mapIndexed { index, maxTime ->
        (0 until maxTime).count { chargeTime ->
            val timeRemaining = maxTime - chargeTime
            val distance = timeRemaining * chargeTime

            return@count (distance > distances[index])
        }
    }

    println("Sum: ${winnable.reduce { acc, i -> acc * i }}")
}