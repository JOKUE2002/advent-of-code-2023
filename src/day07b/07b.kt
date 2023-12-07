package day07b

import java.io.File
import java.util.Collections.max

fun main() {
    val input = File("inputs/07a.txt")
    val lines = input.readLines()

    val hands = lines.map { it.split(' ') }.map { line ->
        val cards = if (line[0].contains('J')) findBestHand(line[0]) else line[0]
        val bid = line[1]

        Triple(cards.toCharArray().toList(), bid.toInt(), line[0].toCharArray().toList())
    }
        .sortedWith(handComparator)

    val sum = hands
        .mapIndexed { index, hand -> hand.second * (index + 1) }
        .sum()

    println("Sum: $sum")


}

fun findBestHand(cardString: String): String {
    val cards = cardString.toCharArray().toList()
    var bestPossibleCardsSingle = cards
    var bestPossibleCardsMultiple = cards

    val cardOptions = cards.filter { it != 'J' }.distinct() + 'A'

    val jIndices = Regex("J").findAll(cardString)
        .map { it.range.first }
        .toList()

    // Check for single J replacements
    for (i in jIndices) {
        for (c in cardOptions) {
            val tmp = bestPossibleCardsSingle.toMutableList()
            tmp[i] = c

            if (sortHands(Triple(bestPossibleCardsSingle, 0, cards), Triple(tmp, 0, cards)) < 0)
                bestPossibleCardsSingle = tmp
        }
    }

    if (jIndices.size > 1) {
        // Check for multi J replacements
        for (c in cardOptions) {
            val tmp = bestPossibleCardsMultiple.toMutableList()

            for (i in jIndices) {
                tmp[i] = c
            }

            if (sortHands(Triple(bestPossibleCardsMultiple, 0, cards), Triple(tmp, 0, cards)) < 0)
                bestPossibleCardsMultiple = tmp
        }
    }

    //println("${cards.joinToString("")} - s: ${bestPossibleCardsSingle.joinToString("")}, m: ${bestPossibleCardsMultiple.joinToString("")}")

    return if (sortHands(Triple(bestPossibleCardsSingle, 0, cards), Triple(bestPossibleCardsMultiple, 0, cards)) >= 0)
        bestPossibleCardsSingle.joinToString("")
    else
        bestPossibleCardsMultiple.joinToString("")
}

val handComparator = Comparator<Triple<List<Char>, Int, List<Char>>> { handA, handB -> sortHands(handA, handB) }

fun sortHands(handA: Triple<List<Char>, Int, List<Char>>, handB: Triple<List<Char>, Int, List<Char>>): Int {
    val evaluationA = evaluateHand(handA.first)
    val evaluationB = evaluateHand(handB.first)

    if (evaluationA == evaluationB) {
        for (i in 0 until 5) {
            val cardValueA = cardValue(handA.third[i])
            val cardValueB = cardValue(handB.third[i])

            if (cardValueA > cardValueB)
                return 1
            else if (cardValueA < cardValueB)
                return -1
        }

        //Hands are equal?
        /*for (i in 0 until 5) {
            val cardValueA = cardValue(handA.third[i])
            val cardValueB = cardValue(handB.third[i])

            if (cardValueA > cardValueB)
                return 1
            else if (cardValueA < cardValueB)
                return -1
        }*/

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

val CARD_VALUES = "J23456789TQKA".toCharArray().toList()
fun cardValue(card: Char): Int = CARD_VALUES.indexOf(card)