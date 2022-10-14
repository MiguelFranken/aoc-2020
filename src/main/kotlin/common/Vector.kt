package common

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Vector(val x: Int, val y: Int) {
    operator fun plus(vector: Vector): Vector = Vector(x + vector.x, y + vector.y)

    operator fun times(scalar: Int): Vector = Vector(x * scalar, y * scalar)

    fun rotate(degree: Int): Vector {
        val radians = Math.toRadians(degree.toDouble())
        // rotation matrix (clockwise rotation)
        return Vector(
            (x * cos(radians) + y * sin(radians)).roundToInt(),
            (-x * sin(radians) + y * cos(radians)).roundToInt()
        )
    }
}
