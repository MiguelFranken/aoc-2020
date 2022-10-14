package task12.a

import common.Direction
import common.Vector
import task12.*
import java.io.File

data class ShipStateA(override val vector: Vector, val heading: Direction): ShipState(vector)

data class MoveDirectionAction(private val instruction: MoveDirection): ShipAction<ShipStateA>({
    val vector = it.vector + when (instruction.direction) {
        Direction.NORTH -> Vector(0, instruction.distance)
        Direction.SOUTH -> Vector(0, -instruction.distance)
        Direction.EAST -> Vector(instruction.distance, 0)
        Direction.WEST -> Vector(-instruction.distance, 0)
    }

    ShipStateA(vector, it.heading)
})

data class MoveForwardAction(private val instruction: MoveForward): ShipAction<ShipStateA>({
    val vector = it.vector + when (it.heading) {
        Direction.NORTH -> Vector(0, instruction.distance)
        Direction.SOUTH -> Vector(0, -instruction.distance)
        Direction.EAST -> Vector(instruction.distance, 0)
        Direction.WEST -> Vector(-instruction.distance, 0)
    }
    ShipStateA(vector, it.heading)
})

data class TurnAction(private val instruction: Turn): ShipAction<ShipStateA>({
    ShipStateA(it.vector, it.heading.turn(instruction.degree))
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

    val state = ShipStateA(Vector(0,0), Direction.EAST).execute(instructions)
    println("Distance: ${state.distance()}")
    require(state.distance() == 757)
}
