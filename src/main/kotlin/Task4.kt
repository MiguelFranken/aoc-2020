import java.io.File

fun main() {
    File("src/main/resources/Task4.txt")
        .readText()
        .trim()
        .split("\n\n", "\r\n\r\n")
        .map(::Passport)
        .let { passports ->
            passports.filter(Passport::hasAllRequiredFields)
            .let { passwordsWithRequiredFields ->
                val validPassports = passwordsWithRequiredFields.filter(Passport::hasValidFields)
                val ratio = validPassports.size.toDouble() / passports.size.toDouble() * 100.0
                println("Number of valid passports: ${validPassports.size}/${passports.size} (${ratio.toInt()}%)")
                require(validPassports.size == 188)

                println()
                println("Example Invalid Passport:")
                val invalidPassports = passwordsWithRequiredFields.filter { !it.hasValidFields() }
                invalidPassports[0].invalidFields().forEach {
                    println("Field '${it.key}' is invalid: ${it.value.second.errorMessage(it.value.first)}")
                }
            }
        }

}

interface Validator {
    fun validate(input: String): Boolean
    fun errorMessage(input: String): String
}

class IntRangeValidator(private val min: Int, private val max: Int): Validator {
    override fun validate(input: String) = input.toIntOrNull()?.let { it in min..max } ?: false

    override fun errorMessage(input: String) = "$input is not in range [$min, $max]"
}

class RegexValidator private constructor(private val pattern: String, private val check: ((MatchResult) -> Boolean)?): Validator {
    override fun validate(input: String) = check?.let {
        Regex(pattern).matchEntire(input)?.let(check) ?: false
    } ?: Regex(pattern).matches(input)

    override fun errorMessage(input: String) = "$input does not match regex $pattern"

    companion object {
        fun create(pattern: String): RegexValidator = RegexValidator(pattern, null)

        fun create(pattern: String, check: ((MatchResult) -> Boolean)) = RegexValidator(pattern, check)
    }
}

class Passport(text: String) {
    private val requiredFields = listOf(
        "byr", "iyr", "eyr",
        "hgt", "hcl", "ecl",
        "pid"
    )

    private val validators = mapOf(
        "byr" to IntRangeValidator(1920, 2002),
        "iyr" to IntRangeValidator(2010, 2020),
        "eyr" to IntRangeValidator(2020, 2030),
        "hcl" to RegexValidator.create("^#([0-9a-z]{6})$"),
        "ecl" to RegexValidator.create("^((amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth))$"),
        "pid" to RegexValidator.create("""^\d{9}$"""),
        "hgt" to RegexValidator.create("""(\d+)(cm|in)""") { result ->
            result.groupValues[1].toIntOrNull()?.let {
                if (result.groupValues[2].contains("cm")) {
                    it in 150..193
                } else {
                    it in 59..76
                }
            } ?: false
        }
    )

    private val map = text.split(" ", "\n", "\r\n").associate {
        Pair(it.substringBefore(":"), it.substringAfter(":"))
    }

    private val validMap = validators.mapValues { (key, validator) -> map[key]?.let(validator::validate) ?: false }

    fun hasAllRequiredFields() = map.keys.containsAll(requiredFields)

    fun hasValidFields() = !validMap.containsValue(false)

    fun invalidFields() = validMap.filterValues { !it }.mapValues { (key, _) -> Pair(map[key]!!, validators[key]!!) }
}
