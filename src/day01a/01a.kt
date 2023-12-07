package day01a

import java.io.File

fun main() {
    val input = File("inputs/01a.txt")
    val lines = input.readLines()

    val txt = lines.map { line ->
        val numberDigits = line.filter { char -> char in '0'..'9' }

        "${numberDigits.first()}${numberDigits.last()}".toInt()
    }

    println("Sum: ${txt.reduce { acc, i -> acc + i }}")
}