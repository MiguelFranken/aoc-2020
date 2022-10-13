import java.io.File
import java.lang.Exception

enum class GridPosition(val char: Char) {
    EMPTY('L'),
    FLOOR('.'),
    OCCUPIED('#');

    companion object {
        fun fromChar(char: Char): GridPosition = values().find { it.char == char } ?: throw Exception("Not Supported")
    }
}

typealias Grid = List<List<GridPosition>>

fun main() {
    val grid: Grid = File("src/main/resources/Task11.txt").readLines().map { line -> line.map(GridPosition::fromChar) }

    var oldGrid = grid.toList()
    var newGrid = grid.toList()
    do {
        oldGrid = newGrid
        newGrid = applyRules(oldGrid)
    } while (oldGrid != newGrid)

    val count = countOccupied(newGrid)
    println("Count: $count")
    require(count == 2319)
}

fun countOccupied(grid: Grid): Int = grid.flatten().count { it == GridPosition.OCCUPIED }

data class Vector(val x: Int, val y: Int)

operator fun Vector.plus(vector: Vector): Vector = Vector(x + vector.x, y + vector.y)

val adjacentVectors = listOf<Vector>(
    Vector(-1, -1), // top left
    Vector(0, -1), // top
    Vector(1, -1), // top right
    Vector(1, 0), // right
    Vector(1, 1), // bottom right
    Vector(0, 1), // bottom
    Vector(-1, 1), // bottom left
    Vector(-1, 0), // left
)

fun applyRules(grid: Grid): Grid {
    val newGrid: MutableList<MutableList<GridPosition>> = grid.map { row -> row.toMutableList() }.toMutableList()

    grid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, cell ->
            val adjacent = adjacentVectors.map { adjacentVector ->
                Vector(columnIndex, rowIndex) + adjacentVector
            }.filter { v ->
                v.x >= 0 && v.x < row.size && v.y >= 0 && v.y < grid.size
            }.map { v ->
                grid[v.y][v.x]
            }

            if (cell == GridPosition.EMPTY && !adjacent.contains(GridPosition.OCCUPIED)) {
                // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
                newGrid[rowIndex][columnIndex] = GridPosition.OCCUPIED
            } else if (cell == GridPosition.OCCUPIED && adjacent.count { it == GridPosition.OCCUPIED } >= 4) {
                // If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
                newGrid[rowIndex][columnIndex] = GridPosition.EMPTY
            }
        }
    }

    return newGrid
}