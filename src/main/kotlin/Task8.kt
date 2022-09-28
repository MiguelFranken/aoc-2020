import java.io.File

enum class Operation(val representation: String) {
    NOP("nop"), ACC("acc"), JMP("jmp");

    companion object {
        fun from(type: String?): Operation = values().find { it.representation == type } ?: NOP
    }
}

typealias InstructionPointer = Int

data class MachineState(val ip: InstructionPointer, val acc: Int)

sealed class Instruction(val action: (MachineState) -> MachineState)

class Nop(val value: Int): Instruction({ MachineState(it.ip + 1, it.acc) })
class Acc(private val value: Int): Instruction({ MachineState(it.ip + 1, it.acc + value) })
class Jmp(val value: Int): Instruction({ MachineState(it.ip + value, it.acc) })

fun Instruction(s: String): Instruction {
    val (op, arg) = s.split(" ")
    val value = arg.toInt()
    return when (Operation.from(op)) {
        Operation.NOP -> Nop(value)
        Operation.ACC -> Acc(value)
        Operation.JMP -> Jmp(value)
    }
}

fun execute(instructions: List<Instruction>): MachineState {
    var state = MachineState(0, 0)

    val encounteredIndices = mutableSetOf<InstructionPointer>()

    while (state.ip in instructions.indices) {
        val nextInstruction = instructions[state.ip]
        state = nextInstruction.action(state)

        if (state.ip in encounteredIndices) return state
        encounteredIndices += state.ip
    }

    println("No loop found! Program terminates!")

    return state
}

fun generateAllMutations(instructions: List<Instruction>): Sequence<List<Instruction>> =
    sequence {
        for ((index, instruction) in instructions.withIndex()) {
            val newProgram = instructions.toMutableList()
            newProgram[index] = when (instruction) {
                is Acc -> continue
                is Jmp -> Nop(instruction.value)
                is Nop -> Jmp(instruction.value)
            }
            yield(newProgram)
        }
    }

fun main() {
    val instructions = File("src/main/resources/Task8.txt").readLines().map(::Instruction)
    val state = execute(instructions)
    println(state)

    val fixedState = generateAllMutations(instructions).map(::execute).first { it.ip !in instructions.indices }
    println(fixedState)
}
