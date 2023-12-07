package day07a

import java.io.File
import java.util.Collections.max

fun main() {
    val input = File("inputs/07a.txt")
    val lines = input.readLines()

    val hands = lines.map { it.split(' ') }.map { line ->
        val cards = line[0]
        val bid = line[1]

        Pair(cards.toCharArray().toList(), bid.toInt())
    }
        .sortedWith(handComparator)

    val sum = hands
        .mapIndexed { index, hand -> hand.second * (index + 1) }
        .sum()

    println("Sum: $sum")


}

val handComparator = Comparator<Pair<List<Char>, Int>> { handA, handB -> sortHands(handA, handB) }

fun sortHands(handA: Pair<List<Char>, Int>, handB: Pair<List<Char>, Int>): Int {
    val evaluationA = evaluateHand(handA.first)
    val evaluationB = evaluateHand(handB.first)

    if (evaluationA == evaluationB) {
        for (i in 0 until 5) {
            val cardValueA = cardValue(handA.first[i])
            val cardValueB = cardValue(handB.first[i])

            if (cardValueA > cardValueB)
                return 1
            else if (cardValueA < cardValueB)
                return -1
        }

        //Hands are equal
        return 0
    } else {
        return evaluationA - evaluationB
    }
}

fun evaluateHand(hand: List<Char>): Int {
    // Five of a Kind
    val map = buildMap<Char, Int> {
        hand.forEach {
            this@buildMap.put(it, this@buildMap.getOrDefault(it, 0) + 1)
        }
    }

    // Five of a Kind
    if (map.values.size == 1)
        return 6

    // Four of a Kind
    if (map.values.size == 2 && max(map.values) == 4)
        return 5

    // Full house
    if (map.values.size == 2 && max(map.values) == 3)
        return 4

    // Three of a kind
    if (map.values.size == 3 && max(map.values) == 3)
        return 3

    // Two Pair
    if (map.values.size == 3 && max(map.values) == 2 && map.values.count { it == 2 } == 2)
        return 2

    // One Pair
    if (map.values.size == 4 && max(map.values) == 2 && map.values.count { it == 2 } == 1)
        return 1

    // High card
    return 0
}

val CARD_VALUES = "23456789TJQKA".toCharArray().toList()
fun cardValue(card: Char): Int = CARD_VALUES.indexOf(card)