package task12.b

import common.Direction
import common.Vector
import task12.*
import task12.MoveDirection
import task12.MoveForward
import task12.Turn
import java.io.File

data class ShipStateB(override val vector: Vector, val waypoint: Vector): ShipState(vector)

data class MoveDirectionAction(private val instruction: MoveDirection): ShipAction<ShipStateB>({
    val vector = it.waypoint + when (instruction.direction) {
        Direction.NORTH -> Vector(0, instruction.distance)
        Direction.SOUTH -> Vector(0, -instruction.distance)
        Direction.EAST -> Vector(instruction.distance, 0)
        Direction.WEST -> Vector(-instruction.distance, 0)
    }

    ShipStateB(it.vector, vector)
})

data class MoveForwardAction(private val instruction: MoveForward): ShipAction<ShipStateB>({
    val vector = it.vector + it.waypoint * instruction.distance
    ShipStateB(vector, it.waypoint)
})

data class TurnAction(private val instruction: Turn): ShipAction<ShipStateB>({
    ShipStateB(it.vector, it.waypoint.rotate(instruction.degree))
})


fun main() {
    val instructions = File("src/main/resources/Task12.txt").readLines().map {
        ShipInstruction.create(it) { instruction ->
            when (instruction) {
                is MoveDirection -> MoveDirectionAction(instruction)
                is MoveForward -> MoveForwardAction(instruction)
                is Turn -> TurnAction(instruction)
            }
        }
    }

    val state = ShipStateB(Vector(0,0), Vector(10,1)).execute(instructions)
    println("Distance: ${state.distance()}")
    require(state.distance() == 51249)
}