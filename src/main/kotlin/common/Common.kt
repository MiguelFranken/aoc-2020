package common

enum class Direction {
    NORTH, EAST,
    SOUTH, WEST;

    /**
     * Turn direction by specified degree.
     * Allowed degrees: Multiplies of 90
     */
    fun turn(degree: Int): Direction = values()[(((values().indexOf(this) + degree/90) % 4) + 4) % 4]
}
