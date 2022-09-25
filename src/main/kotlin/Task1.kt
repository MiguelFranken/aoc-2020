import java.io.File

fun List<Int>.findPairOfSum(sum: Int): Pair<Int, Int>? {
    val complements = associateBy { sum - it }
    return firstNotNullOfOrNull { x ->
        complements[x]?.let { y -> Pair(x, y) }
    }
}

fun List<Int>.findTripleOfSum(sum: Int): Triple<Int, Int, Int>? {
    return firstNotNullOfOrNull { x ->
        findPairOfSum(sum - x)?.let { (y, z) ->
            Triple(x, y, z)
        }
    }
}

fun List<Int>.product(): Int = reduce(Int::times)

fun printNumbers(numbers: List<Int>) {
    println("${numbers.joinToString(" + ")} = ${numbers.sum()}")
    println("${numbers.joinToString(" * ")} = ${numbers.product()}")
}

fun Triple<Int, Int, Int>.printNumbers() = printNumbers(toList())
fun Pair<Int, Int>.printNumbers() = printNumbers(toList())

fun main() {
    val numbers = File("src/main/resources/Task1.txt").readLines().mapNotNull(String::toIntOrNull)
    numbers.findPairOfSum(2020)?.printNumbers()
    numbers.findTripleOfSum(2020)?.printNumbers()
}
