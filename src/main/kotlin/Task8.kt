import java.io.File

enum class Operation(val representation: String) {
    NOP("nop"), ACC("acc"), JMP("jmp");

    companion object {
        fun from(type: String?): Operation = values().find { it.representation == type } ?: NOP
    }
}
data class Instruction(val operation: Operation, val arg: Int) {
    companion object {
        fun fromString(line: String): Instruction {
            val (op, arg) = line.split(" ")
            return Instruction(Operation.from(op), arg.toInt())
        }
    }
}

fun main() {
    val value = getAccumulatorValue()
    println("Accumulator Value: $value")
}

fun getAccumulatorValue(): Int {
    val operations = File("src/main/resources/Task8.txt").readLines().map(Instruction::fromString)

    val visited = mutableSetOf<Int>()
    var accumulator = 0
    var counter = 0
    while (counter < operations.size && !visited.contains(counter)) {
        visited += counter

        val instruction = operations[counter]
        when (instruction.operation) {
            Operation.NOP -> counter++
            Operation.ACC -> {
                counter++
                accumulator += instruction.arg
            }
            Operation.JMP -> counter += instruction.arg
        }
    }

    return accumulator
}
