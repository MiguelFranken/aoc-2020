import java.io.File

internal const val WINDOW_SIZE = 25

fun List<Long>.hasPairOfSum(sum: Long): Boolean =
    indices.any { i ->
        indices.any { j ->
            i != j && this[i] + this[j] == sum
        }
    }

fun List<Long>.findSublistOfSum(sum: Long): List<Long>? =
    indices.firstNotNullOfOrNull { fromIndex ->
        ((fromIndex + 1)..size).firstNotNullOfOrNull { toIndex ->
            subList(fromIndex, toIndex).takeIf { it.sum() == sum }
        }
    }

fun main() {
    val numbers = File("src/main/resources/Task9.txt").readLines().map(String::toLong)

    val invalidNumber = numbers.asSequence().windowed(WINDOW_SIZE + 1).firstOrNull {
        val prevGroup = it.dropLast(1)
        !prevGroup.hasPairOfSum(it.last())
    }?.last()!!

    println("First Invalid Number: $invalidNumber")

    val encryptionWeakness = numbers.findSublistOfSum(invalidNumber)?.let { it.min() + it.max() }
    println("Encryption Weakness: $encryptionWeakness")
}
