package task12.a

import common.Vector
import java.io.File
import java.lang.Exception
import kotlin.math.abs


enum class Direction {
    NORTH, EAST,
    SOUTH, WEST;

    /**
     * Turn direction by specified degree.
     * Allowed degrees: Multiplies of 90
     */
    fun turn(degree: Int): Direction = values()[(((values().indexOf(this) + degree/90) % 4) + 4) % 4]
}

data class ShipState(val vector: Vector, val heading: Direction) {
    fun distance(): Int = abs(vector.x) + abs(vector.y)
}

sealed class ShipInstruction(val action: (ShipState) -> ShipState)

fun String.splitAtIndex(index: Int) = take(index) to substring(index)

fun ShipInstruction(s: String): ShipInstruction {
    val (ins, value) = s.splitAtIndex(1).let { Pair(it.first, it.second.toInt()) }
    return when (ins) {
        "F" -> MoveForward(value)
        "N" -> MoveDirection(Direction.NORTH, value)
        "E" -> MoveDirection(Direction.EAST, value)
        "S" -> MoveDirection(Direction.SOUTH, value)
        "W" -> MoveDirection(Direction.WEST, value)
        "L" -> Turn(-value)
        "R" -> Turn(value)
        else -> throw Exception("Operation Not Supported")
    }
}

data class MoveDirection(
    private val direction: Direction,
    private val distance: Int
): ShipInstruction({
    val vector = it.vector + when (direction) {
        Direction.NORTH -> Vector(0, distance)
        Direction.SOUTH -> Vector(0, -distance)
        Direction.EAST -> Vector(distance, 0)
        Direction.WEST -> Vector(-distance, 0)
    }

    ShipState(vector, it.heading)
})

data class MoveForward(private val distance: Int): ShipInstruction({
    val vector = it.vector + when (it.heading) {
        Direction.NORTH -> Vector(0, distance)
        Direction.SOUTH -> Vector(0, -distance)
        Direction.EAST -> Vector(distance, 0)
        Direction.WEST -> Vector(-distance, 0)
    }
    ShipState(vector, it.heading)
})

data class Turn(private val degree: Int): ShipInstruction({
    ShipState(it.vector, it.heading.turn(degree))
})

fun main() {
    val instructions = File("src/main/resources/Task12.txt").readLines().map(::ShipInstruction)
    val state = execute(instructions)
    println("Distance: ${state.distance()}")
    require(state.distance() == 757)
}

fun execute(instructions: List<ShipInstruction>): ShipState {
    var state = ShipState(Vector(0,0), Direction.EAST)
    instructions.forEach { instruction ->
        state = instruction.action(state)
    }
    return state
}