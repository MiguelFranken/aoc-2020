package task12

import common.Direction
import common.Vector
import common.splitAtIndex
import task12.a.ShipStateA
import java.lang.Exception
import kotlin.math.abs

abstract class ShipState(open val vector: Vector) {
    fun distance(): Int = abs(vector.x) + abs(vector.y)
}

sealed interface ShipInstruction {
    companion object {
        fun <S: ShipState> create(s: String, actionFactory: (instruction: ShipInstruction) -> ShipAction<S>): ShipAction<S> {
            val (ins, value) = s.splitAtIndex(1).let { Pair(it.first, it.second.toInt()) }

            val instruction = when (ins) {
                "F" -> MoveForward(value)
                "N" -> MoveDirection(Direction.NORTH, value)
                "E" -> MoveDirection(Direction.EAST, value)
                "S" -> MoveDirection(Direction.SOUTH, value)
                "W" -> MoveDirection(Direction.WEST, value)
                "L" -> Turn(-value)
                "R" -> Turn(value)
                else -> throw Exception("Operation Not Supported")
            }

            return actionFactory(instruction)
        }
    }
}

abstract class ShipAction<S: ShipState>(val apply: (S) -> S)

data class MoveForward(val distance: Int): ShipInstruction
data class MoveDirection(
    val direction: Direction,
    val distance: Int
): ShipInstruction
data class Turn(val degree: Int): ShipInstruction

fun <S: ShipState> S.execute(actions: List<ShipAction<S>>): S {
    var state = this
    actions.forEach { action ->
        state = action.apply(state)
    }
    return state
}