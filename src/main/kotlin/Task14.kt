import common.toBinary
import java.io.File

fun main() {
    val input = File("src/main/resources/Task14.txt").readLines()
    val answerPart1 = Task14(input).solvePart1()
    println("Answer Part 1: $answerPart1")
    require(answerPart1 == 11884151942312L)

    val answerPart2 = Task14(input).solvePart2()
    println("Answer Part 2: $answerPart2")
    require(answerPart2 == 2625449018811L)
}

class Task14(private val input: List<String>) {
    private val memory: MutableMap<Long, Long> = mutableMapOf()

    private infix fun String.maskedWith(mask: String): Long =
        toBinary().zip(mask).map { (valueChar, maskChar) ->
            maskChar.takeUnless { it == 'X' } ?: valueChar
        }.joinToString("").toLong(2)

    private fun String.generateAddressMasks(mask: String): List<Long> {
        val addresses = mutableListOf(toBinary().toCharArray())

        mask.forEachIndexed { idx, bit ->
            when (bit) {
                '1' -> addresses.forEach { it[idx] = '1' }
                'X' -> {
                    addresses.forEach { it[idx] = '1' }
                    addresses.addAll(
                        addresses.map {
                            it.copyOf().apply { this[idx] = '0' }
                        }
                    )
                }
            }
        }

        return addresses.map { it.joinToString("").toLong(2) }
    }

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

    fun solvePart2(): Long {
        var mask = DEFAULT_MASK
        input.forEach { instruction ->
            if (instruction.startsWith("mask")) {
                mask = instruction.substringAfter("= ")
            } else {
                val (unmaskedAddress, value) = memoryRegex.find(instruction)!!.destructured
                memory += unmaskedAddress.generateAddressMasks(mask).associateWith { value.toLong() }
            }
        }

        return memory.values.sum()
    }

    companion object {
        const val DEFAULT_MASK = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

        private val memoryRegex = """mem\[(\d+)] = (\d+)""".toRegex()
    }
}