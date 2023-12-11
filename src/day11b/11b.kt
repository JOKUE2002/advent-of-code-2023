package day11b

import java.io.File
import kotlin.math.abs

typealias Galaxy = Pair<Long, Long>

fun main() {
    val input = File("inputs/11a.txt")
    val lines = input.readLines()

    val galaxies = buildList<Galaxy> {

        var currentYOffset = 0L
        lines.forEachIndexed { y, line ->
            if (line.none { it == '#' })
                currentYOffset += 999_999L

            var currentXOffset = 0L
            line.forEachIndexed { x, c ->
                if (lines.none { it[x] == '#' })
                    currentXOffset  += 999_999L

                if (c == '#')
                    this@buildList.add(Galaxy(x + currentXOffset, y + currentYOffset))
            }
        }
    }

    val galaxyPairs = galaxies.flatMapIndexed { i1, g1 -> galaxies.filterIndexed { index, _ -> index > i1 }.map { g2 -> Pair(g1, g2) } }

    val sum = galaxyPairs.map { it.first.distanceTo(it.second) }

    println("Sum: ${sum.sum()}")
}

fun Galaxy.distanceTo(other: Galaxy): Long = abs(this.first - other.first) + abs(this.second - other.second)