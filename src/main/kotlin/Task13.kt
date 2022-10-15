import java.io.File

fun main() {
    executeTask1()
}

fun executeTask1() {
    val lines = File("src/main/resources/Task13.txt").readLines()
    val depart = lines.first().toInt()
    val busIds = lines.last().split(",").filterNot { it == "x" }.map(String::toInt)

    val diffs = busIds.map { bus -> bus to depart / bus * bus - depart + bus }

    val earliestBus = diffs.minBy { it.second }
    val answer = earliestBus.first * earliestBus.second
    println(answer)
    require(answer == 5257)
}