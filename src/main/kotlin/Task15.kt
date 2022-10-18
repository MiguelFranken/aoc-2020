class Task15(private val input: List<Int>) {
    private fun memoryGame(): Sequence<Int> = sequence {
        yieldAll(input)
        val memory = input.mapIndexed { idx, number -> number to idx }.toMap().toMutableMap()
        var turns = input.size
        var sayNext = 0
        while (true) {
            yield(sayNext)
            val lastTimeSpoken = memory[sayNext] ?: turns
            memory[sayNext] = turns
            sayNext = turns - lastTimeSpoken
            turns++
        }
    }

    fun solvePart1(): Int = memoryGame().drop(2020 - 1).first()
}