import kotlin.test.Test
import kotlin.test.assertEquals

internal class Task15Test {
    @Test
    fun `Test Input 2,1,3`() {
        val input = listOf(2, 1, 3)
        val answer = Task15(input).solvePart1()
        assertEquals(10, answer)
    }
}