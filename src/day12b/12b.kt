package day12b

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import skipFirst
import java.io.File

val CACHE = mutableMapOf<String, Long>()
fun main() {
    val inputFile = File("inputs/12a.txt")
    val lines = inputFile.readLines()


    runBlocking {
        var lineNo = 0
        val counts = lines.map { line ->
            async(Dispatchers.Default) {
                println("Starting $lineNo of ${lines.size}")
                lineNo++

                val parts = line.split(" ")
                val startInput = parts[0]
                val startGoal = parts[1].split(",").map(String::toLong)

                val input = "$startInput?$startInput?$startInput?$startInput?$startInput"
                val goal = startGoal + startGoal + startGoal + startGoal + startGoal

                val r = calculateRecursive(input, goal)
                println("Finished $lineNo of ${lines.size}")
                r
            }
        }.awaitAll()

        println(counts)
        println(CACHE)

        println("Sum: ${counts.sum()}")
    }
}

/**
 * Goal entries will be removed if succeeded
 */
fun calculateRecursive(input: String, goal: List<Long>): Long {
    if (input.length == 0)
        return if (goal.isEmpty()) 1 else 0

    /*if (CACHE.containsKey(input))
        return CACHE[input]!!*/

    val tmp: Long

    if (input.first() == '.') {
        tmp = calculateRecursive(input.substring(1), goal)
    } else if (input.first() == '?') {
        tmp = calculateRecursive('.' + input.substring(1), goal) + calculateRecursive('#' + input.substring(1), goal)
    } else {
        // First char is #

        if (goal.isEmpty()) {
            // We don't have anymore to fill, so this won't work
            CACHE["0"] = CACHE["0"]?.plus(1L) ?: 1L
            tmp = 0
        } else {
            if (goal.sum().toInt() > input.count { it == '#' || it == '?' } || goal.sum() > input.length) {
                // Those are some impossible configs, so we can cancel early
                CACHE["1"] = CACHE["1"]?.plus(1L) ?: 1L
                tmp = 0
            } else {
                if (input.all { it == '#' || it == '?' } && input.count() == goal.first().toInt()) {
                    CACHE["2"] = CACHE["2"]?.plus(1L) ?: 1L

                    tmp = if (goal.size == 1) 1 else 0
                } else if (input[goal.first().toInt()] == '.') {
                    CACHE["3"] = CACHE["3"]?.plus(1L) ?: 1L

                    tmp = calculateRecursive(input.substring(goal.first().toInt()), goal.skipFirst())
                } else if (input[goal.first().toInt()] == '?') {
                    CACHE["4"] = CACHE["4"]?.plus(1L) ?: 1L

                    tmp = calculateRecursive('.' + input.substring(goal.first().toInt() + 1), goal.skipFirst())
                } else {
                    // Invalid because next after our group would be another # (illegal combination)
                    CACHE["5"] = CACHE["5"]?.plus(1L) ?: 1L

                    tmp = 0
                }
            }
        }
    }

    //CACHE[input] = tmp

    return tmp
}

private operator fun String.set(index: Int, char: Char) {
    this.substring(0..<index) + char + this.substring(index + 1)
}

fun isValid(chars: List<Char>, goal: List<Long>): Boolean {
    return buildList<Long> {
        var currentCount = 0L

        for (char in chars) {
            if (char == '#') {
                currentCount++
            } else if (currentCount > 0) {
                this@buildList.add(currentCount)
                currentCount = 0
            }
        }

        if (currentCount > 0) {
            this@buildList.add(currentCount)
        }
    } == goal
}