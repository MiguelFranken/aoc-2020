import kotlin.test.Test
import kotlin.test.assertEquals

internal class Task12Test {
    @Test
    fun `Can turn direction by degree`() {
        assertEquals(Direction.SOUTH, Direction.NORTH.turn(-180))
        assertEquals(Direction.WEST, Direction.NORTH.turn(-90))
        assertEquals(Direction.NORTH, Direction.NORTH.turn(0))
        assertEquals(Direction.EAST, Direction.NORTH.turn(90))
        assertEquals(Direction.SOUTH, Direction.NORTH.turn(180))
        assertEquals(Direction.WEST, Direction.NORTH.turn(270))
        assertEquals(Direction.NORTH, Direction.NORTH.turn(360))

        assertEquals(Direction.SOUTH, Direction.EAST.turn(90))
        assertEquals(Direction.WEST, Direction.EAST.turn(180))
        assertEquals(Direction.NORTH, Direction.EAST.turn(270))
        assertEquals(Direction.EAST, Direction.EAST.turn(360))

        assertEquals(Direction.WEST, Direction.SOUTH.turn(90))
        assertEquals(Direction.NORTH, Direction.SOUTH.turn(180))
        assertEquals(Direction.EAST, Direction.SOUTH.turn(270))
        assertEquals(Direction.SOUTH, Direction.SOUTH.turn(360))

        assertEquals(Direction.NORTH, Direction.WEST.turn(90))
        assertEquals(Direction.EAST, Direction.WEST.turn(180))
        assertEquals(Direction.SOUTH, Direction.WEST.turn(270))
        assertEquals(Direction.WEST, Direction.WEST.turn(360))
    }
}
