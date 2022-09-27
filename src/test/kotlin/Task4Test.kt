import kotlin.test.Test
import kotlin.test.assertFalse

internal class Task4Test {
    @Test
    fun testIntRangeValidator() {
        val validator = IntRangeValidator(10, 20)

        val falseInputs = listOf("9", "a", "21", "0", "-1")
        val trueInputs = listOf("10", "15", "20")

        falseInputs.forEach { assertFalse { validator.validate(it) } }
        trueInputs.forEach { validator.validate(it) }
    }
}
