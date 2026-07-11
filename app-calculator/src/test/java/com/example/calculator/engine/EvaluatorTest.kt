package com.example.calculator.engine

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class EvaluatorTest {
    private fun eval(expr: String): BigDecimal? {
        val tokens = Tokenizer(expr).tokenize()
        val result = Evaluator.evaluate(tokens)
        assertNull(result.error)
        return result.value
    }

    @Test
    fun testBasicAddition() {
        assertEquals(BigDecimal("5"), eval("2+3"))
    }

    @Test
    fun testBasicSubtraction() {
        assertEquals(BigDecimal("1"), eval("3-2"))
    }

    @Test
    fun testBasicMultiplication() {
        assertEquals(BigDecimal("6"), eval("2*3"))
    }

    @Test
    fun testBasicDivision() {
        assertEquals(BigDecimal("2"), eval("6/3"))
    }

    @Test
    fun testPrecedence() {
        assertEquals(BigDecimal("7"), eval("1+2*3"))
    }

    @Test
    fun testParentheses() {
        assertEquals(BigDecimal("9"), eval("(1+2)*3"))
    }

    @Test
    fun testDecimal() {
        assertEquals(BigDecimal("3.5"), eval("1.5+2"))
    }

    @Test
    fun testNegate() {
        assertEquals(BigDecimal("-5"), eval("-5"))
    }

    @Test
    fun testNegateWithAddition() {
        assertEquals(BigDecimal("3"), eval("5+-2"))
    }

    @Test
    fun testPercent() {
        assertEquals(BigDecimal("0.5"), eval("50%"))
    }

    @Test
    fun testFactorial() {
        assertEquals(BigDecimal("120"), eval("5!"))
    }

    @Test
    fun testPower() {
        assertEquals(BigDecimal("8"), eval("2^3"))
    }

    @Test
    fun testSinDegree() {
        Evaluator.isDegreeMode = true
        val result = eval("sin(30)")
        assertEquals(BigDecimal("0.5"), result!!.setScale(1, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun testCosDegree() {
        Evaluator.isDegreeMode = true
        val result = eval("cos(60)")
        assertEquals(BigDecimal("0.5"), result!!.setScale(1, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun testSqrt() {
        assertEquals(BigDecimal("4"), eval("sqrt(16)"))
    }

    @Test
    fun testPi() {
        val result = eval("π")
        assertNotNull(result)
    }

    @Test
    fun testDivisionByZero() {
        val tokens = Tokenizer("1/0").tokenize()
        val result = Evaluator.evaluate(tokens)
        assertEquals("除数不能为零", result.error)
    }

    @Test
    fun testSquare() {
        assertEquals(BigDecimal("9"), eval("square(3)"))
    }

    @Test
    fun testCube() {
        assertEquals(BigDecimal("27"), eval("cube(3)"))
    }

    @Test
    fun testComplexExpression() {
        assertEquals(BigDecimal("23"), eval("(2+3)*4+3"))
    }

    @Test
    fun testMultipleNegate() {
        assertEquals(BigDecimal("5"), eval("--5"))
    }
}
