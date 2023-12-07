package day06b

import java.io.File

fun main() {
    val input = File("inputs/06a.txt")
    val lines = input.readLines()

    val maxTime = Regex("\\d+")
        .findAll(lines[0])
        .map(MatchResult::value)
        .joinToString("")
        .toLong()

    val maxDistance = Regex("\\d+")
        .findAll(lines[1])
        .map(MatchResult::value)
        .joinToString("")
        .toLong()

    val winCount = (0 until maxTime).count { chargeTime ->
            val timeRemaining = maxTime - chargeTime
            val distance = timeRemaining * chargeTime

            return@count (distance > maxDistance)
        }

    println("Sum: $winCount")
}