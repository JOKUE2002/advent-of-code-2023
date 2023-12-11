package day09b

import java.io.File


/**
 * a - b
 *   c
 *
 * b - a = c
 * b = a + c
 * a = b - c
 */
fun main() {
    val input = File("inputs/09a.txt")
    val lines = input.readLines()

    val lists = lines.map { it.split(" ").map(String::toInt).toMutableList() }

    val sum = lists.sumOf { getPreviousNumber(it) }

    println("Sum: $sum")
}

fun getPreviousNumber(list: MutableList<Int>): Int {
    val layers = mutableListOf(list)

    do {
        val newLayer = layers.last().windowed(2) { (a, b) -> b - a }.toMutableList()
        layers.add(newLayer)
    } while (newLayer.any { it != 0 })

    for (index in layers.size - 2 downTo 0) {
        val b = layers[index].first()
        val c = layers[index + 1].first()
        val a = b - c

        layers[index].add(0, a)
    }

    return layers.first().first()
}