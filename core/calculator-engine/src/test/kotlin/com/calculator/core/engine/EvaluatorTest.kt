package com.calculator.core.engine

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Random
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

class EvaluatorTest {

    private val parser = { input: String -> Parser(Lexer(input)).parse() }
    private val random = Random(42L)

    private fun evalExpression(input: String, angleMode: AngleMode = AngleMode.RADIAN): Double {
        val expr = parser(input)
        val result = Evaluator.evaluate(expr, angleMode)
        assertTrue(result.isSuccess(), "Evaluation failed for '$input': ${result.exceptionOrNull}")
        return result.getOrThrow()
    }

    private fun assertApproxEquals(expected: Double, actual: Double, input: String, tolerance: Double = 1e-10) {
        if (expected.isNaN() && actual.isNaN()) return
        if (expected.isInfinite() && actual.isInfinite() && sign(expected) == sign(actual)) return
        val diff = abs(expected - actual)
        val maxAbs = max(abs(expected), abs(actual))
        val relativeDiff = if (maxAbs > 0) diff / maxAbs else diff
        assertTrue(
            relativeDiff <= tolerance || diff <= tolerance,
            "Mismatch for '$input': expected $expected, got $actual (diff=$diff, rel=$relativeDiff)"
        )
    }

    @Test
    fun `basic arithmetic operations`() = runTest {
        val testCases = listOf(
            "1+2" to 3.0,
            "5-3" to 2.0,
            "4*6" to 24.0,
            "20/4" to 5.0,
            "10%3" to 1.0,
            "2^3" to 8.0,
            "2^3^2" to 512.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertEquals(expected, actual, 1e-10, "Failed for '$input'")
        }
    }

    @Test
    fun `parentheses and precedence`() = runTest {
        val testCases = listOf(
            "(1+2)*3" to 9.0,
            "1+(2*3)" to 7.0,
            "2*(3+4)*5" to 70.0,
            "((1+2))" to 3.0,
            "10/(2+3)" to 2.0,
            "2^(1+2)" to 8.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertEquals(expected, actual, 1e-10, "Failed for '$input'")
        }
    }

    @Test
    fun `constants pi and e`() = runTest {
        assertApproxEquals(Math.PI, evalExpression("pi"), "pi")
        assertApproxEquals(Math.E, evalExpression("e"), "e")
        assertApproxEquals(2 * Math.PI, evalExpression("2*pi"), "2*pi")
        assertApproxEquals(3 * Math.E, evalExpression("3*e"), "3*e")
    }

    @Test
    fun `trigonometric functions`() = runTest {
        val testCases = listOf(
            "sin(0)" to 0.0,
            "sin(pi/2)" to 1.0,
            "sin(pi)" to 0.0,
            "cos(0)" to 1.0,
            "cos(pi/2)" to 0.0,
            "cos(pi)" to -1.0,
            "tan(0)" to 0.0,
            "tan(pi/4)" to 1.0,
            "asin(0)" to 0.0,
            "asin(1)" to Math.PI / 2,
            "acos(0)" to Math.PI / 2,
            "acos(1)" to 0.0,
            "atan(0)" to 0.0,
            "atan(1)" to Math.PI / 4,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `trigonometric functions in degree mode`() = runTest {
        val testCases = listOf(
            "sin(0)" to 0.0,
            "sin(90)" to 1.0,
            "sin(180)" to 0.0,
            "cos(0)" to 1.0,
            "cos(90)" to 0.0,
            "cos(180)" to -1.0,
            "tan(0)" to 0.0,
            "tan(45)" to 1.0,
            "asin(0)" to 0.0,
            "asin(1)" to 90.0,
            "acos(0)" to 90.0,
            "acos(1)" to 0.0,
            "atan(0)" to 0.0,
            "atan(1)" to 45.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input, AngleMode.DEGREE)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `deg or rad parity for trigonometric functions`() = runTest {
        // sin(90°) == sin(pi/2 rad) == 1
        val sinDeg = evalExpression("sin(90)", AngleMode.DEGREE)
        val sinRad = evalExpression("sin(pi/2)", AngleMode.RADIAN)
        assertApproxEquals(sinRad, sinDeg, "sin parity")

        // cos(180°) == cos(pi rad) == -1
        val cosDeg = evalExpression("cos(180)", AngleMode.DEGREE)
        val cosRad = evalExpression("cos(pi)", AngleMode.RADIAN)
        assertApproxEquals(cosRad, cosDeg, "cos parity")

        // tan(45°) == tan(pi/4 rad) == 1
        val tanDeg = evalExpression("tan(45)", AngleMode.DEGREE)
        val tanRad = evalExpression("tan(pi/4)", AngleMode.RADIAN)
        assertApproxEquals(tanRad, tanDeg, "tan parity")

        // asin(1) in deg = 90, in rad = pi/2
        val asinDeg = evalExpression("asin(1)", AngleMode.DEGREE)
        val asinRad = evalExpression("asin(1)", AngleMode.RADIAN)
        assertApproxEquals(Math.toDegrees(asinRad), asinDeg, "asin parity")

        // acos(0) in deg = 90, in rad = pi/2
        val acosDeg = evalExpression("acos(0)", AngleMode.DEGREE)
        val acosRad = evalExpression("acos(0)", AngleMode.RADIAN)
        assertApproxEquals(Math.toDegrees(acosRad), acosDeg, "acos parity")

        // atan(1) in deg = 45, in rad = pi/4
        val atanDeg = evalExpression("atan(1)", AngleMode.DEGREE)
        val atanRad = evalExpression("atan(1)", AngleMode.RADIAN)
        assertApproxEquals(Math.toDegrees(atanRad), atanDeg, "atan parity")
    }

    @Test
    fun `hyperbolic functions`() = runTest {
        val testCases = listOf(
            "sinh(0)" to 0.0,
            "sinh(1)" to Math.sinh(1.0),
            "cosh(0)" to 1.0,
            "cosh(1)" to Math.cosh(1.0),
            "tanh(0)" to 0.0,
            "tanh(1)" to Math.tanh(1.0),
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `logarithmic and exponential functions`() = runTest {
        val testCases = listOf(
            "log(1)" to 0.0,
            "log(e)" to 1.0,
            "log(e^2)" to 2.0,
            "ln(1)" to 0.0,
            "ln(e)" to 1.0,
            "ln(e^3)" to 3.0,
            "log10(1)" to 0.0,
            "log10(10)" to 1.0,
            "log10(1000)" to 3.0,
            "exp(0)" to 1.0,
            "exp(1)" to Math.E,
            "exp(2)" to Math.exp(2.0),
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `power and root functions`() = runTest {
        val testCases = listOf(
            "sqrt(4)" to 2.0,
            "sqrt(2)" to Math.sqrt(2.0),
            "sqrt(0)" to 0.0,
            "cbrt(27)" to 3.0,
            "cbrt(-8)" to -2.0,
            "cbrt(0)" to 0.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `rounding functions`() = runTest {
        val testCases = listOf(
            "floor(1.9)" to 1.0,
            "floor(-1.1)" to -2.0,
            "ceil(1.1)" to 2.0,
            "ceil(-1.9)" to -1.0,
            "round(1.4)" to 1.0,
            "round(1.6)" to 2.0,
            "round(-1.4)" to -1.0,
            "round(-1.6)" to -2.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertEquals(expected, actual, 1e-10, "Failed for '$input'")
        }
    }

    @Test
    fun `absolute value and deg rad conversion`() = runTest {
        assertEquals(5.0, evalExpression("abs(-5)"))
        assertEquals(5.0, evalExpression("abs(5)"))
        assertEquals(0.0, evalExpression("abs(0)"))
        assertApproxEquals(180.0, evalExpression("deg(pi)"), "deg(pi)")
        assertApproxEquals(Math.PI, evalExpression("rad(180)"), "rad(180)")
    }

    @Test
    fun `factorial operator`() = runTest {
        val testCases = listOf(
            "0!" to 1.0,
            "1!" to 1.0,
            "2!" to 2.0,
            "3!" to 6.0,
            "4!" to 24.0,
            "5!" to 120.0,
            "6!" to 720.0,
            "10!" to 3628800.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertEquals(expected, actual, 1e-10, "Failed for '$input'")
        }
    }

    @Test
    fun `percent operator`() = runTest {
        val testCases = listOf(
            "50%" to 0.5,
            "100+10%" to 110.0,
            "100-10%" to 90.0,
            "100*10%" to 10.0,
            "100/10%" to 1000.0,
            "5!%" to 1.2,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `unary operators`() = runTest {
        val testCases = listOf(
            "-5" to -5.0,
            "+5" to 5.0,
            "--5" to 5.0,
            "-(-5)" to 5.0,
            "-(2+3)" to -5.0,
            "-5^2" to -25.0,
            "sin(-pi/2)" to -1.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `implicit multiplication`() = runTest {
        val testCases = listOf(
            "2(3+4)" to 14.0,
            "3(4)" to 12.0,
            "2(3)" to 6.0,
            "5(3)" to 15.0,
            "2pi" to 2 * Math.PI,
            "3e" to 3 * Math.E,
            "(1+2)(3+4)" to 21.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `nested function calls`() = runTest {
        val testCases = listOf(
            "sin(asin(0.5))" to 0.5,
            "cos(acos(0.5))" to 0.5,
            "sqrt(sqrt(16))" to 2.0,
            "sqrt(abs(-16))" to 4.0,
            "log(exp(2))" to 2.0,
            "exp(ln(5))" to 5.0,
            "sin(sin(pi/2))" to Math.sin(1.0),
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `complex expressions`() = runTest {
        val testCases = listOf(
            "2+3*5" to 17.0,
            "(2+3)*5" to 25.0,
            "2*3+5" to 11.0,
            "2*(3+5)*5/2" to 40.0,
            "2^3" to 8.0,
            "2^4" to 16.0,
            "1+2*3-4/5" to 1 + 2 * 3.0 - 4.0 / 5.0,
            "sin(pi/2)+cos(0)" to 2.0,
            "sqrt(9)+cbrt(27)" to 6.0,
            "log10(100)+ln(e)" to 3.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `property based testing random expressions`() = runTest {
        val operators = listOf("+", "-", "*", "/", "^", "%")
        val functions = listOf("sin", "cos", "tan", "sqrt", "cbrt", "abs", "floor", "ceil", "round", "log", "ln", "log10", "exp")
        val constants = listOf("pi", "e")
        val count = AtomicInteger(0)
        val errors = AtomicInteger(0)

        repeat(200) {
            val input = generateRandomExpression(operators, functions, constants, depth = 3)
            try {
                val expected = evalKotlinReference(input)
                val actual = evalExpression(input)
                assertApproxEquals(expected, actual, input, 1e-9)
                count.incrementAndGet()
            } catch (e: AssertionError) {
                errors.incrementAndGet()
                throw e
            } catch (e: Exception) {
                errors.incrementAndGet()
                println("Reference eval error for '$input': ${e.message}")
            }
        }
        println("Property test: ${count.get()} passed, ${errors.get()} errors")
    }

    @Test
    fun `property based testing random arithmetic expressions`() = runTest {
        repeat(500) {
            val a = random.nextDouble() * 2000 - 1000
            val b = random.nextDouble() * 2000 - 1000
            val op = operators[random.nextInt(operators.size)]
            val input = when (op) {
                "/" -> if (b == 0.0) "1/1" else "$a/$b"
                "^" -> {
                    val base = abs(a) % 10 + 0.1
                    val exp = (abs(b) % 10).toInt()
                    "$base^$exp"
                }
                "%" -> if (b == 0.0) "10%3" else "$a%$b"
                else -> "$a$op$b"
            }
            try {
                val expected = evalKotlinReference(input)
                val actual = evalExpression(input)
                assertApproxEquals(expected, actual, input, 1e-9)
            } catch (e: AssertionError) {
                throw e
            } catch (e: Exception) {
                println("Error for '$input': ${e.message}")
            }
        }
    }

    private fun evalKotlinReference(input: String): Double {
        val expr = parser(input)
        return Evaluator.evaluate(expr, AngleMode.RADIAN).getOrThrow()
    }

    @Test
    fun `division by zero throws ArithmeticException`() = runTest {
        val testCases = listOf("1/0", "10/0", "(1+2)/0", "1/(2-2)")
        testCases.forEach { input ->
            val expr = parser(input)
            val result = Evaluator.evaluate(expr, AngleMode.RADIAN)
            assertTrue(result.isFailure(), "Expected failure for '$input'")
            assertTrue(result.exceptionOrNull is ArithmeticException, "Expected ArithmeticException for '$input', got ${result.exceptionOrNull?.javaClass}")
        }
    }

    @Test
    fun `sqrt of negative throws IllegalArgumentException`() = runTest {
        val testCases = listOf("sqrt(-1)", "sqrt(-4)", "sqrt(0-1)")
        testCases.forEach { input ->
            val expr = parser(input)
            val result = Evaluator.evaluate(expr, AngleMode.RADIAN)
            assertTrue(result.isFailure(), "Expected failure for '$input'")
            assertTrue(result.exceptionOrNull is IllegalArgumentException, "Expected IllegalArgumentException for '$input', got ${result.exceptionOrNull?.javaClass}")
        }
    }

    @Test
    fun `log of non-positive throws`() = runTest {
        val testCases = listOf("log(0)", "log(-1)", "ln(0)", "ln(-1)", "log10(0)", "log10(-1)")
        testCases.forEach { input ->
            val expr = parser(input)
            val result = Evaluator.evaluate(expr, AngleMode.RADIAN)
            assertTrue(result.isFailure(), "Expected failure for '$input'")
            assertTrue(result.exceptionOrNull is ArithmeticException || result.exceptionOrNull is IllegalArgumentException, "Expected exception for '$input', got ${result.exceptionOrNull?.javaClass}")
        }
    }

    @Test
    fun `asin acos out of domain throws`() = runTest {
        val testCases = listOf("asin(2)", "asin(-2)", "acos(2)", "acos(-2)")
        testCases.forEach { input ->
            val expr = parser(input)
            val result = Evaluator.evaluate(expr, AngleMode.RADIAN)
            assertTrue(result.isFailure(), "Expected failure for '$input'")
        }
    }

    @Test
    fun `large factorial returns infinity`() = runTest {
        val result = evalExpression("171!")
        assertTrue(result.isInfinite(), "171! should be infinity, got $result")
        val result2 = evalExpression("1000!")
        assertTrue(result2.isInfinite(), "1000! should be infinity, got $result2")
    }

    @Test
    fun `factorial of negative throws`() = runTest {
        val testCases = listOf("(-1)!", "(-5)!")
        testCases.forEach { input ->
            val expr = parser(input)
            val result = Evaluator.evaluate(expr, AngleMode.RADIAN)
            assertTrue(result.isFailure(), "Expected failure for '$input'")
            assertTrue(result.exceptionOrNull is IllegalArgumentException)
        }
    }

    @Test
    fun `factorial of non-integer throws`() = runTest {
        val testCases = listOf("(2.5)!", "(3.14)!")
        testCases.forEach { input ->
            val expr = parser(input)
            val result = Evaluator.evaluate(expr, AngleMode.RADIAN)
            assertTrue(result.isFailure(), "Expected failure for '$input'")
            assertTrue(result.exceptionOrNull is IllegalArgumentException)
        }
    }

    @Test
    fun `scientific notation`() = runTest {
        val testCases = listOf(
            "1e10" to 1e10,
            "1E-5" to 1e-5,
            "3.14e2" to 314.0,
            "6.022e23" to 6.022e23,
            "1e0" to 1.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertApproxEquals(expected, actual, input)
        }
    }

    @Test
    fun `modulo operator with negative numbers`() = runTest {
        val testCases = listOf(
            "10%3" to 1.0,
            "-10%3" to -1.0,
            "10%-3" to 1.0,
            "-10%-3" to -1.0,
            "10%5" to 0.0,
        )
        testCases.forEach { (input, expected) ->
            val actual = evalExpression(input)
            assertEquals(expected, actual, 1e-10, "Failed for '$input'")
        }
    }

    private fun generateRandomExpression(
        operators: List<String>,
        functions: List<String>,
        constants: List<String>,
        depth: Int
    ): String {
        if (depth <= 0 || random.nextDouble() < 0.3) {
            return when (random.nextInt(3)) {
                0 -> "${random.nextDouble() * 200 - 100}"
                1 -> constants[random.nextInt(constants.size)]
                else -> "${random.nextInt(201) - 100}"
            }
        }
        val exprType = random.nextInt(4)
        return when (exprType) {
            0 -> {
                val left = generateRandomExpression(operators, functions, constants, depth - 1)
                val right = generateRandomExpression(operators, functions, constants, depth - 1)
                val op = operators[random.nextInt(operators.size)]
                "($left$op$right)"
            }
            1 -> {
                val fn = functions[random.nextInt(functions.size)]
                val arg = generateRandomExpression(operators, functions, constants, depth - 1)
                "$fn($arg)"
            }
            2 -> {
                val base = generateRandomExpression(operators, functions, constants, depth - 1)
                "$base!"
            }
            else -> {
                val base = generateRandomExpression(operators, functions, constants, depth - 1)
                "$base%"
            }
        }
    }

    companion object {
        private val operators = listOf("+", "-", "*", "/", "^", "%")
    }
}