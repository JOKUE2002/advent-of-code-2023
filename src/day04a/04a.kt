package day04a

import java.io.File
import kotlin.math.pow

private data class Card (
    val id: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>
) {
    fun getValue(): Int {
        val cnt = numbers.count { winningNumbers.contains(it) }

        return if (cnt == 0)
            0
        else
            2.0.pow((cnt - 1).toDouble()).toInt()
    }
}

fun main() {
    val input = File("inputs/04a.txt")
    val lines = input.readLines()

    val cards = lines.map { line ->
        val tmp = line.split(":")
        val id = tmp[0].replace("""Card +""".toRegex(), "")

        val winningNumbers = tmp[1].split(" | ")[0].split(' ').filterNot(String::isBlank)
        val myNumbers = tmp[1].split(" | ")[1].split(' ').filterNot(String::isBlank)

        Card(id.toInt(), winningNumbers.map(String::toInt), myNumbers.map(String::toInt))
    }

    println("Sum: ${cards.map(Card::getValue).sum()}")
}