import common.toBinary
import java.io.File

fun main() {
    val input = File("src/main/resources/Task14.txt").readLines()
    val answer = Task14(input).solvePart1()
    println(answer)
}

class Task14(private val input: List<String>) {
    private val memory: MutableMap<Long, Long> = mutableMapOf()

    private infix fun String.maskedWith(mask: String): Long =
        toBinary().zip(mask).map { (valueChar, maskChar) ->
            maskChar.takeUnless { it == 'X' } ?: valueChar
        }.joinToString("").toLong(2)

    fun solvePart1(): Long {
        var mask = DEFAULT_MASK
        input.forEach { instruction ->
            if (instruction.startsWith("mask")) {
                mask = instruction.substringAfter("= ")
            } else {
                val (address, value) = memoryRegex.find(instruction)!!.destructured
                memory[address.toLong()] = value maskedWith mask
            }
        }

        return memory.values.sum()
    }

    companion object {
        const val DEFAULT_MASK = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

        private val memoryRegex = """mem\[(\d+)] = (\d+)""".toRegex()
    }
}