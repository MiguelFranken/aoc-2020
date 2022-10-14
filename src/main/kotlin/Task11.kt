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
typealias MutableGrid = MutableList<MutableList<GridPosition>>

fun main() {
    task1()
    task2()
}

fun task1() {
    val grid: Grid = File("src/main/resources/Task11.txt").readLines().map { line -> line.map(GridPosition::fromChar) }
    val transformed = grid.applyUntilStabilizes(Grid::applyRulesSimple)
    val count = countOccupied(transformed)
    println("Count Task 2: $count")
    require(count == 2319)
}

fun task2() {
    val grid: Grid = File("src/main/resources/Task11.txt").readLines().map { line -> line.map(GridPosition::fromChar) }
    val transformed = grid.applyUntilStabilizes(Grid::applyRules)
    val count = countOccupied(transformed)
    println("Count Task 2: $count")
    require(count == 2117)
}

fun Grid.applyUntilStabilizes(apply: Grid.() -> Grid): Grid {
    var oldGrid: Grid
    var newGrid = toList()
    do {
        oldGrid = newGrid
        newGrid = apply(oldGrid)
    } while (oldGrid != newGrid)

    return newGrid
}

fun countOccupied(grid: Grid): Int = grid.flatten().count { it == GridPosition.OCCUPIED }

data class Vector(val x: Int, val y: Int)

operator fun Vector.plus(vector: Vector): Vector = Vector(x + vector.x, y + vector.y)

operator fun Vector.times(scalar: Int): Vector = Vector(x * scalar, y * scalar)

val directions = listOf<Vector>(
    Vector(-1, -1), // top left
    Vector(0, -1), // top
    Vector(1, -1), // top right
    Vector(1, 0), // right
    Vector(1, 1), // bottom right
    Vector(0, 1), // bottom
    Vector(-1, 1), // bottom left
    Vector(-1, 0), // left
)

fun Grid.toMutable(): MutableGrid = map { row -> row.toMutableList() }.toMutableList()

fun Grid.findVisibleSeat(start: Vector, direction: Vector, bound: Boolean = false): GridPosition? {
    var currentVector = start + direction
    while (currentVector.x in this[0].indices && currentVector.y in indices) {
        val position = this[currentVector.y][currentVector.x]
        if (position == GridPosition.OCCUPIED || position == GridPosition.EMPTY) {
            return position
        }
        if (bound) {
            return null
        }

        currentVector += direction
    }

    return null
}

fun Grid.computeVisibleSeats(bound: Boolean = false): List<List<List<GridPosition>>> =
    mapIndexed { rowIndex, row ->
        List(row.size) { columnIndex ->
            directions.mapNotNull { direction -> findVisibleSeat(Vector(columnIndex, rowIndex), direction, bound) }
        }
    }

fun Grid.applyRules(): Grid {
    val transformedGrid: MutableGrid = toMutable()
    val positions = computeVisibleSeats()

    forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, cell ->
            val visibleSeats = positions[rowIndex][columnIndex]

            if (cell == GridPosition.EMPTY && !visibleSeats.contains(GridPosition.OCCUPIED)) {
                // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
                transformedGrid[rowIndex][columnIndex] = GridPosition.OCCUPIED
            } else if (cell == GridPosition.OCCUPIED && visibleSeats.count { it == GridPosition.OCCUPIED } >= 5) {
                // five or more visible occupied seats for an occupied seat to become empty
                transformedGrid[rowIndex][columnIndex] = GridPosition.EMPTY
            }
        }
    }

    return transformedGrid
}

fun Grid.applyRulesSimple(): Grid {
    val transformedGrid: MutableGrid = toMutable()
    val positions = computeVisibleSeats(true)

    forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, cell ->
            val visibleSeats = positions[rowIndex][columnIndex]

            if (cell == GridPosition.EMPTY && !visibleSeats.contains(GridPosition.OCCUPIED)) {
                // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
                transformedGrid[rowIndex][columnIndex] = GridPosition.OCCUPIED
            } else if (cell == GridPosition.OCCUPIED && visibleSeats.count { it == GridPosition.OCCUPIED } >= 4) {
                // If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
                transformedGrid[rowIndex][columnIndex] = GridPosition.EMPTY
            }
        }
    }

    return transformedGrid
}