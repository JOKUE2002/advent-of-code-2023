package day08a

import java.io.File

data class Node (
    val name: String,
    val left: String,
    val right: String
)

val REGEX = """(\w+) = \((\w+), (\w+)\)""".toRegex()
fun String.toNode(): Node? {
    val res = REGEX.find(this) ?: return null

    val (a, b, c) = res.destructured
    return Node(a, b, c)
}

fun main() {
    val input = File("inputs/08a.txt")
    val lines = input.readLines()

    val instructions = lines[0].map { it }

    val nodes = lines.subList(2, lines.size).mapNotNull(String::toNode)

    var current = "AAA"
    var iterations = 0
    while (current != "ZZZ") {
        instructions.forEach { instruction ->
            iterations++

            current = if (instruction == 'L')
                nodes.find { it.name == current }?.left ?: throw Error("No node found")
            else
                nodes.find { it.name == current }?.right ?: throw Error("No node found")

            if (current == "ZZZ")
                return@forEach
        }
    }

    println("Sum: $iterations")
}