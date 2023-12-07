package day02b

import java.io.File

data class Game (
    val id: Int,
    val records: List<Record>
) {
    fun getRedMax(): Int = records.maxOf(Record::red)
    fun getGreenMax(): Int = records.maxOf(Record::green)
    fun getBlueMax(): Int = records.maxOf(Record::blue)

    fun getPower(): Int = getRedMax() * getGreenMax() * getBlueMax()
}

data class Record (
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0
)

fun main() {
    val input = File("inputs/02a.txt")
    val lines = input.readLines()

    val games = lines.map { line ->
        val gameID = line.split(':')[0].split(' ')[1]

        val records = line
            .split(": ")[1].split("; ")
            .map { rec ->

                val entries = rec.split(", ").map { it.split(" ") }

                Record(
                    entries.find { it[1] == "red" }
                        ?.get(0)
                        ?.toIntOrNull() ?: 0,
                    entries.find { it[1] == "green" }
                        ?.get(0)
                        ?.toIntOrNull() ?: 0,
                    entries.find { it[1] == "blue" }
                        ?.get(0)
                        ?.toIntOrNull() ?: 0
                )
            }

        Game(gameID.toInt(), records)
    }

    println("Sum: ${games.sumOf(Game::getPower)}")
}