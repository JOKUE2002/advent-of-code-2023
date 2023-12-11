package day10b

import java.io.File
import java.util.Collections.max
import java.util.Collections.min
import kotlin.Error
import kotlin.math.abs

data class Position (
    val x: Int,
    val y: Int
) {
    fun add(other: Position):Position = Position(this.x + other.x, this.y + other.y)

    fun neighbor(position: Position): Boolean = ((abs(position.x - x) <= 1 && position.y == y) xor (abs(position.y - y) <= 1 && position.x == x))
    fun neighborDirection(position: Position): Direction? {
        if (!neighbor(position))
            return null

        when (x - position.x) {
            -1 -> return Direction.EAST
            1 -> return Direction.WEST
        }

        when (y - position.y) {
            -1 -> return Direction.SOUTH
            1 -> return Direction.NORTH
        }

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false

        return this.x == other.x && this.y == other.y
    }
}

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun flip(): Direction = when(this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }

    fun offset(): Position = when (this) {
        NORTH -> Position(0, -1)
        EAST -> Position(1, 0)
        SOUTH -> Position(0, 1)
        WEST -> Position(-1, 0)
    }
}

enum class Pipe(val char: Char, val directions: Set<Direction>) {
    NORTH_EAST('L', setOf(Direction.NORTH, Direction.EAST)),
    NORTH_WEST('J', setOf(Direction.NORTH, Direction.WEST)),

    SOUTH_EAST('F', setOf(Direction.SOUTH, Direction.EAST)),
    SOUTH_WEST('7', setOf(Direction.SOUTH, Direction.WEST)),

    NORTH_SOUTH('|', setOf(Direction.NORTH, Direction.SOUTH)),
    EAST_WEST('-', setOf(Direction.EAST, Direction.WEST)),

    START('S', emptySet()),
    EMPTY('.', emptySet());

    fun nextDirection(lastDirection: Direction): Direction = directions.first { it != lastDirection }
    override fun toString(): String = "$char"

    companion object {
        fun fromChar(char: Char) = entries.first { it.char == char }
        fun fromDirections(directions: Set<Direction>): Pipe = entries.first { it.directions == directions }
    }
}

data class Tile (
    val position: Position,
    val pipe: Pipe
) {
    fun connects(other: Tile): Boolean = this.pipe.directions.any { this.position.add(it.offset()) == other.position }

    /**
     * https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
     */
    fun isInPath(path: List<Tile>): Boolean {
        var count = 0
        var previous = path.last()
        path.forEach { current ->
            if (previous.position.y > position.y != current.position.y > position.y && position.x < (current.position.x - previous.position.x) * (position.y - previous.position.y) / (current.position.y - previous.position.y) + previous.position.x)
                count++

            previous = current
        }
        return count % 2 != 0
    }
}

fun main() {
    val input = File("inputs/10a.txt")
    val lines = input.readLines()

    val tiles = lines.flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x: Int, c: Char ->
            Tile(Position(x, y), Pipe.fromChar(c))
        }
    }

    val startTile = tiles.first { it.pipe == Pipe.START }

    val tilesConnectingToStart = tiles
        .filter { it.position.neighbor(startTile.position) }
        .filter { it.connects(startTile) }

    val circle = buildList<Tile> {
        add(startTile)
        add(tilesConnectingToStart.first())

        do {
            val current = this@buildList.last()
            val previous = this@buildList.dropLast(1).last()
            val lastDirection = previous.position.neighborDirection(current.position) ?: throw Error("No direction found")

            val nextDirection = current.pipe.nextDirection(lastDirection.flip())

            val offset = nextDirection.offset()

            val nextPosition = current.position.add(offset)

            val next = tiles.first { it.position == nextPosition }

            this@buildList.add(next)
        } while (next.pipe != Pipe.START)
    }.dropLast(1)

    val startPipe = Pipe.fromDirections(setOf(
        startTile.position.neighborDirection(tilesConnectingToStart.first().position)!!,
        startTile.position.neighborDirection(tilesConnectingToStart.last().position)!!
    ))

    val insideTiles = mutableSetOf<Tile>()
    val minX = min(circle.map { it.position.x })
    val minY = min(circle.map { it.position.y })
    val maxX = max(circle.map { it.position.x })
    val maxY = max(circle.map { it.position.y })
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val tile = tiles.find { it.position == Position(x, y) } ?: throw Error("No Tile found at $x $y")

            if (tile !in circle && tile.isInPath(circle))
                insideTiles.add(tile)
        }
    }

    println(insideTiles)
    println("Inside count: ${insideTiles.size}")
}
