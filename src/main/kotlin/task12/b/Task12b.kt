package task12.b

import common.Vector
import common.splitAtIndex
import task12.a.Direction
import java.io.File
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class ShipState(val vector: Vector, val waypoint: Vector) {
    fun distance(): Int = abs(vector.x) + abs(vector.y)
}

sealed class ShipInstruction(val action: (ShipState) -> ShipState)

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
    val vector = it.waypoint + when (direction) {
        Direction.NORTH -> Vector(0, distance)
        Direction.SOUTH -> Vector(0, -distance)
        Direction.EAST -> Vector(distance, 0)
        Direction.WEST -> Vector(-distance, 0)
    }

    ShipState(it.vector, vector)
})

data class MoveForward(private val distance: Int): ShipInstruction({
    val vector = it.vector + it.waypoint * distance
    ShipState(vector, it.waypoint)
})

data class Turn(private val degree: Int): ShipInstruction({
    ShipState(it.vector, it.waypoint.rotate(degree))
})

fun main() {
    val instructions = File("src/main/resources/Task12.txt").readLines().map(::ShipInstruction)
    val state = execute(instructions)
    println("Distance: ${state.distance()}")
    require(state.distance() == 51249)
}

fun execute(instructions: List<ShipInstruction>): ShipState {
    var state = ShipState(Vector(0, 0), Vector(10, 1))
    instructions.forEach { instruction ->
        state = instruction.action(state)
    }
    return state
}

fun Vector.rotate(degree: Int): Vector {
    val radians = Math.toRadians(degree.toDouble())
    // rotation matrix (clockwise rotation)
    return Vector(
        (x * cos(radians) + y * sin(radians)).roundToInt(),
        (-x * sin(radians) + y * cos(radians)).roundToInt()
    )
}