package day09a

import java.io.File


/**
 * a - b
 *   c
 *
 * b - a = c
 * b = a + c
 */
fun main() {
    val input = File("inputs/09a.txt")
    val lines = input.readLines()

    val lists = lines.map { it.split(" ").map(String::toInt).toMutableList() }

    val sum = lists.sumOf { getNextNumber(it) }

    println("Sum: $sum")
}

fun getNextNumber(list: MutableList<Int>): Int {
    val layers = mutableListOf(list)

    do {
        val newLayer = layers.last().windowed(2) { (a, b) -> b - a }.toMutableList()
        layers.add(newLayer)
    } while (newLayer.any { it != 0 })

    for (index in layers.size - 2 downTo 0) {
        val a = layers[index].last()
        val c = layers[index + 1].last()
        val b = a + c

        layers[index].add(b)
    }

    return layers.first().last()
}