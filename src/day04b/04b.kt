package day04b

import java.io.File
import kotlin.math.pow

private data class Card (
    val id: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>
) {
    fun getWinningCount() : Int = numbers.count { winningNumbers.contains(it) }
    fun getValue(): Int = if (getWinningCount() == 0) 0 else 2.0.pow((getWinningCount() - 1).toDouble()).toInt()
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

    var count = 0
    cards.forEach { card ->
        count += processCard(card, cards)
    }

    println("Count: $count")
}

private fun processCard(card: Card, cards: List<Card>): Int {
    var cnt = 1 // Current Card

    for (i in 1..card.getWinningCount()) {
        cards.find { it.id == (card.id + i) }?.let { cnt += processCard(it, cards) }
    }

    return cnt
}