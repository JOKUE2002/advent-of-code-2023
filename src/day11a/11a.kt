package day11a

import java.io.File
import kotlin.math.abs

typealias Galaxy = Pair<Int, Int>

fun main() {
    val input = File("inputs/11a.txt")
    val lines = input.readLines()

    val galaxies = buildList<Galaxy> {

        var currentYOffset = 0
        lines.forEachIndexed { y, line ->
            if (line.none { it == '#' })
                currentYOffset++

            var currentXOffset = 0
            line.forEachIndexed { x, c ->
                if (lines.none { it[x] == '#' })
                    currentXOffset++

                if (c == '#')
                    this@buildList.add(Galaxy(x + currentXOffset, y + currentYOffset))
            }
        }
    }

    val galaxyPairs = galaxies.flatMapIndexed { i1, g1 -> galaxies.filterIndexed { index, _ -> index > i1 }.map { g2 -> Pair(g1, g2) } }

    val sum = galaxyPairs.map { it.first.distanceTo(it.second) }

    println("Sum: ${sum.sum()}")
}

fun Galaxy.distanceTo(other: Galaxy): Int = abs(this.first - other.first) + abs(this.second - other.second)