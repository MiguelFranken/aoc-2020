import java.io.File

data class PasswordWithPolicy(val password: String, val range: IntRange, val letter: Char) {
    fun validate() = password.count { it == letter } in range

    companion object {
        private val regex = Regex("""(\d+)-(\d+) ([a-z]): ([a-z]+)""")

        fun parse(line: String): PasswordWithPolicy {
            val (policy, password) = line.split(":").map(String::trim)
            val letter = line.substringAfter(" ").substringBefore(":").single()
            val (min, max) = policy.substringBeforeLast(" ").split("-").map(String::toInt)
            return PasswordWithPolicy(password, min..max, letter)
        }

        fun parseWithRegex(line: String): PasswordWithPolicy = regex.matchEntire(line)!!
            .destructured
            .let { (start, end, letter, password) ->
                PasswordWithPolicy(password, start.toInt()..end.toInt(), letter.single())
            }
    }
}

fun main() {
    val lines = File("src/main/resources/Task2.txt").readLines()

    //parse
    val passwords = lines.map(PasswordWithPolicy::parse)
    val passwordsParsedWithRegex = lines.map(PasswordWithPolicy::parseWithRegex)

    // validate
    val matching = passwords.filter(PasswordWithPolicy::validate)
    val matchingWithRegex = passwordsParsedWithRegex.filter(PasswordWithPolicy::validate)

    // validate correctness
    require(matching.size == 542)
    require(matchingWithRegex.size == 542)
}
