import kotlin.test.Test
import kotlin.test.assertEquals

internal class Task5Test {
    @Test
    fun computeRow() {
        val rows = mapOf(
            "FBFBBFFRLR" to 44,
            "BFFFBBFRRR" to 70,
            "FFFBBBFRRR" to 14,
            "BBFFBBFRLL" to 102
        )
        
        rows.forEach { (specification, row) ->
            assertEquals(row, getRow(specification))
        }
    }

    @Test()
    fun computeColumn() {
        val columns = mapOf(
            "FBFBBFFRLR" to 5,
            "BFFFBBFRRR" to 7,
            "FFFBBBFRRR" to 7,
            "BBFFBBFRLL" to 4
        )

        columns.forEach { (specification, column) ->
            assertEquals(column, getColumn(specification))
        }
    }

    @Test()
    fun computeSeatIds() {
        val ids = mapOf(
            "FBFBBFFRLR" to 357,
            "BFFFBBFRRR" to 567,
            "FFFBBBFRRR" to 119,
            "BBFFBBFRLL" to 820
        )

        ids.forEach { (specification, id) ->
            assertEquals(id, getSeatId(specification))
        }
    }

    @Test()
    fun computeHighestSeatId() {
        val specifications = listOf("FBFBBFFRLR", "BFFFBBFRRR", "FFFBBBFRRR", "BBFFBBFRLL")
        assertEquals(820, getHighestSeatId(specifications))
    }
}
