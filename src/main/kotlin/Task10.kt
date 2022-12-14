import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

fun main() {
    part1()
    part2()
}

private fun part1() {
    val jolts = File("src/main/resources/Task10.txt").readLines().map(String::toInt).toMutableList().apply {
        this += 0
        this += max() + 3
    }.toList().sorted()

    val differences = jolts.windowed(2).map { it.last() - it.first() }
    val part1 = differences.count { it == 1 } * differences.count { it == 3 }

    logger.info { "Part 1: $part1" }
}

fun part2() {
    val jolts = File("src/main/resources/Task10.txt").readLines().map(String::toInt).toMutableList().apply {
        this += max() + 3
    }.toList().sorted()

    // Dynamic Programming: Memoize number of paths to jolt n
    // #paths_0_to_n = #paths_0_to_n-1 + #paths_0_to_n-2 + #paths_0_to_n-3
    val options: MutableMap<Int, Long> = mutableMapOf(0 to 1L)
    jolts.forEach { targetJolts ->
        val sum = listOf(1,2,3).sumOf { difference ->
            options[targetJolts - difference] ?: 0
        }
        options += targetJolts to sum
    }

    logger.info {
        val part2 = options[jolts.max()]
        "Part 2: $part2"
    }
}
