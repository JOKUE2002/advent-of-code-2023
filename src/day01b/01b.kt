package day01b

import java.io.File
import java.util.Collections.max
import java.util.Collections.min

data class FoundElement (
    val element: Int,
    val start: Int,
    val end: Int
)

fun main() {
    val input = File("inputs/01a.txt")
    val lines = input.readLines()
    val numbers = arrayOf(Pair("one", 1), Pair("two", 2), Pair("three", 3), Pair("four", 4), Pair("five", 5), Pair("six", 6), Pair("seven", 7), Pair("eight", 8), Pair("nine", 9), Pair("1", 1), Pair("2", 2), Pair("3", 3), Pair("4", 4), Pair("5", 5), Pair("6", 6), Pair("7", 7), Pair("8", 8), Pair("9", 9))

    val txt = lines.map { line ->
        val numIndices = numbers.map { FoundElement(it.second, line.indexOf(it.first), line.lastIndexOf(it.first)) }

        val minIndex = min(numIndices.map(FoundElement::start).filter { it > -1 })
        val maxIndex = max(numIndices.map(FoundElement::end).filter { it > -1 })

        "${numIndices.find { it.start == minIndex }?.element}${numIndices.find { it.end == maxIndex }?.element}".toInt()
    }

    println("Sum: ${txt.reduce { acc, i -> acc + i }}")
}