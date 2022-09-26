import java.io.File

data class Slope(val x: Int, val y: Int)

fun main() {
    val field = File("src/main/resources/Task3.txt").readLines()
    val width = field[0].length

    val slopes = listOf(
        Slope(1, 1),
        Slope(3, 1),
        Slope(5, 1),
        Slope(7, 1),
        Slope(1, 2),
    )

    val trees = slopes.map { slope ->
        fun x(y: Int) = (slope.x * (y / slope.y)) % width
        field.indices.filter { it % slope.y == 0}.count { y -> field[y][x(y)] == '#' }
    }

    println(trees)

    val answer = trees.map(Int::toLong).reduce(Long::times)
    println("Answer: $answer")
}
