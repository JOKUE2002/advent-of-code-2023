package day12a

import java.io.File
import kotlin.math.pow

fun main() {
    val inputFile = File("inputs/12a.txt")
    val lines = inputFile.readLines()

    val counts = lines.map { line ->
        val parts = line.split(" ")
        val input = parts[0]
        val goal = parts[1].split(",").map(String::toInt)

        val count = input.count { it == '?' }
        (0..<2f.pow(count).toInt()).count { i ->
            val tmp = input.map { it }.toMutableList()
            val binary = Integer.toBinaryString(i).padStart(count, '0')

            for (c in binary) {
                tmp[tmp.indexOf('?')] = if (c == '1') '#' else '.'
            }

            isValid(tmp, goal)
        }
    }

    println("Sum: ${counts.sum()}")
}

private operator fun String.set(index: Int, char: Char) {
    this.substring(0..<index) + char + this.substring(index + 1)
}

fun isValid(chars: List<Char>, goal: List<Int>): Boolean {
    return buildList<Int> {
        var currentCount = 0

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