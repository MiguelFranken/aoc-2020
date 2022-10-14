package common

data class Vector(val x: Int, val y: Int) {
    operator fun plus(vector: Vector): Vector = Vector(x + vector.x, y + vector.y)

    operator fun times(scalar: Int): Vector = Vector(x * scalar, y * scalar)
}
