import java.io.File
import kotlin.math.pow

fun getRow(specification: String): Int = specification
    .filter { it == 'F' || it == 'B' }
    .map { it == 'B' }
    .binarySpacePartitioning(7) // [0,127] = [0,2^7]

fun getColumn(specification: String): Int = specification
    .filter { it == 'L' || it == 'R' }
    .map { it == 'R' }
    .binarySpacePartitioning(3) // [0,8] = [0,2^3]

fun List<Boolean>.binarySpacePartitioning(digits: Int): Int = fold(
    Pair(0, (2.0.pow(digits) - 1).toInt())
) { acc, curr ->
    val a = (acc.second - acc.first + 1) / 2
    when (curr) {
        false -> Pair(acc.first, acc.first + a - 1) // lower half
        true -> Pair(acc.first + a, acc.second) // upper half
    }
}.first

fun getSeatId(specification: String) = getSeatId(getRow(specification), getColumn(specification))

fun getSeatId(row: Int, column: Int) = row * 8 + column

fun getHighestSeatId(specifications: List<String>): Int = specifications.maxOf(::getSeatId)

fun main() {
    val specifications = File("src/main/resources/Task5.txt").readLines()
    val maxId = getHighestSeatId(specifications)
    println("Highest Seat ID: $maxId")

    val seatIds = specifications.map(::getSeatId).toSet()
    fun isOccupied(seatId: Int) = seatId in seatIds

    val mySeatId = (1..maxId).find {
        !isOccupied(it) && isOccupied(it - 1) && isOccupied(it + 1)
    }
    println("My Seat ID: $mySeatId")
}
