package day08b

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

fun kgv(x: Long, y: Long): Long = (x * y) / ggt(x, y)

fun ggt(x: Long, y: Long): Long {
    var num1 = x
    var num2 = y

    while (num2 != 0L) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }

    return num1
}



fun main() {
    val input = File("inputs/08a.txt")
    val lines = input.readLines()

    val instructions = lines[0].map { it }

    val nodes = lines.subList(2, lines.size).mapNotNull(String::toNode)

    fun findNode(name: String): Node = nodes.find { it.name == name } ?: throw Error("No node found")

    val startingNodes = nodes.filter { it.name.endsWith('A') }.toMutableList()
    val distances = buildList<Long> {
        this@buildList.add(1)

        startingNodes.forEach { currentStart ->
            var current = currentStart
            var distance = 0L

            while (!current.name.endsWith('Z')) {
                current = if (instructions[(distance % instructions.size).toInt()] == 'L') findNode(current.left) else findNode(current.right)

                distance++
            }

            this@buildList.add(distance)
        }
    }



    println("Sum: ${distances.reduce { acc, l -> kgv(acc, l) }}")
}






fun dnf1(nodes: List<Node>, instructions: List<Char>, findNode: (String) -> Node) {
    val current = nodes.filter { it.name.endsWith('A') }.toMutableList()
    var iterations = 0
    while (current.any { !it.name.endsWith('Z') }) {
        instructions.forEach { instruction ->
            iterations++

            if (instruction == 'L') {
                current.forEachIndexed { index, node ->
                    current[index] = findNode(findNode(node.name).left)
                }
            } else {
                current.forEachIndexed { index, node ->
                    current[index] = findNode(findNode(node.name).right)
                }
            }

            if (current.all { it.name.endsWith('Z') })
                return@forEach

            if (current.any { it.name.endsWith('Z') })
                println("Partial: $current $iterations")
        }
    }
}