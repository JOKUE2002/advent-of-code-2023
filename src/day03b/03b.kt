package day03b

import java.io.File
import kotlin.math.abs

private data class Symbol (
    val symbol: String,
    val range: IntRange,
    val line: Int
)

fun main() {
    val input = File("inputs/03a.txt")
    val lines = input.readLines()

    val numberRegex = """\d+""".toRegex()
    val symbolRegex = """[^\d.]""".toRegex()

    val allSymbols = buildList<Symbol> {
        lines.forEachIndexed { index, line ->
            val symbols = symbolRegex.findAll(line)

            symbols.forEach {
                this@buildList.add(Symbol(it.value, it.range, index))
            }
        }
    }

    val allNumbers = buildList<Symbol> {
        lines.forEachIndexed { index, line ->
            val numbers = numberRegex.findAll(line)

            numbers.forEach {
                this@buildList.add(Symbol(it.value, it.range, index))
            }
        }
    }

    val out = allSymbols.filter { it.symbol == "*" }.map { symbol ->
        val matchingNumbers = allNumbers.filter { number -> ((number.range.first - 1)..(number.range.last + 1)).any { it == symbol.range.first } && abs(number.line - symbol.line) <= 1}
        if (matchingNumbers.size == 2) {
            val tmp = matchingNumbers.map(Symbol::symbol).map(String::toInt).reduce { acc, i -> acc * i }
            tmp
        } else {
            0
        }
    }

    //println(allSymbols)

    /*val allNumbers = buildList<Int> {
        lines.forEachIndexed { index, line ->
            val numbers = numberRegex.findAll(line)

            numbers.forEach {
                if (allSymbols.any { symbol -> ((it.range.first - 1)..(it.range.last + 1)).any { it == symbol.range.first } && abs(symbol.line - index) <= 1}) {
                    this@buildList.add(it.value.toInt())
                }
            }
        }
    }*/

    println("Sum: ${out.sum()}")
}