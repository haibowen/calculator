package com.calculator.core.engine

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ParserTest {

    private fun parse(input: String): Expr {
        return Parser(Lexer(input)).parse()
    }

    private fun evaluate(input: String): Double {
        return Evaluator.evaluate(parse(input), AngleMode.RADIAN).getOrThrow()
    }

    @Test
    fun `parse operator precedence - multiplication before addition`() {
        assertEquals(7.0, evaluate("1+2*3"))
        assertEquals(7.0, evaluate("2*3+1"))
        assertEquals(14.0, evaluate("2+3*4"))
        assertEquals(11.0, evaluate("2*3+5"))
    }

    @Test
    fun `parse right-associative power operator`() {
        assertEquals(512.0, evaluate("2^3^2")) // 2^(3^2) = 2^9 = 512
        assertEquals(256.0, evaluate("2^2^3")) // 2^(2^3) = 2^8 = 256
        assertEquals(81.0, evaluate("3^2^2")) // 3^(2^2) = 3^4 = 81
    }

    @Test
    fun `parse parentheses override precedence`() {
        assertEquals(9.0, evaluate("(1+2)*3"))
        assertEquals(7.0, evaluate("1+(2*3)"))
        assertEquals(18.0, evaluate("(1+2)*(3+3)"))
        assertEquals(1.0, evaluate("((1))"))
    }

    @Test
    fun `parse function calls`() {
        assertEquals(1.0, evaluate("sin(pi/2)"), 1e-10)
        assertEquals(0.0, evaluate("cos(pi/2)"), 1e-10)
        assertEquals(1.0, evaluate("tan(pi/4)"), 1e-10)
        assertEquals(0.0, evaluate("sin(0)"), 1e-10)
        assertEquals(1.0, evaluate("cos(0)"), 1e-10)
        assertEquals(2.0, evaluate("sqrt(4)"), 1e-10)
        assertEquals(3.0, evaluate("cbrt(27)"), 1e-10)
        assertEquals(1.0, evaluate("log(e)"), 1e-10)
        assertEquals(2.0, evaluate("log10(100)"), 1e-10)
        assertEquals(1.0, evaluate("ln(e)"), 1e-10)
        assertEquals(2.718281828459045, evaluate("exp(1)"), 1e-10)
    }

    @Test
    fun `parse factorial operator`() {
        assertEquals(120.0, evaluate("5!"))
        assertEquals(1.0, evaluate("0!"))
        assertEquals(1.0, evaluate("1!"))
        assertEquals(2.0, evaluate("2!"))
        assertEquals(6.0, evaluate("3!"))
        assertEquals(24.0, evaluate("4!"))
        assertEquals(720.0, evaluate("6!"))
    }

    @Test
    fun `parse percent operator`() {
        assertEquals(110.0, evaluate("100+10%")) // 100 + (10% of 100) = 110
        assertEquals(0.5, evaluate("50%")) // 50% = 0.5
        assertEquals(150.0, evaluate("100+50%")) // 100 + (50% of 100) = 150
        assertEquals(0.05, evaluate("5%"), 1e-10)
        assertEquals(90.0, evaluate("100-10%")) // 100 - (10% of 100) = 90
        assertEquals(10.0, evaluate("100*10%")) // 100 * 0.1 = 10
        assertEquals(1000.0, evaluate("100/10%")) // 100 / 0.1 = 1000
    }

    @Test
    fun `parse unary minus`() {
        assertEquals(-25.0, evaluate("-5^2")) // -(5^2) = -25, not (-5)^2 = 25
        assertEquals(-5.0, evaluate("-5"))
        assertEquals(5.0, evaluate("--5"))
        assertEquals(-3.0, evaluate("-(1+2)"))
        assertEquals(-1.0, evaluate("sin(-pi/2)"), 1e-10)
    }

    @Test
    fun `parse implicit multiplication`() {
        assertEquals(14.0, evaluate("2(3+4)"))
        assertEquals(12.0, evaluate("3(4)"))
        assertEquals(6.0, evaluate("2(3)"))
        assertEquals(15.0, evaluate("5(3)"))
    }

    @Test
    fun `parse complex expressions with multiple operators`() {
        assertEquals(17.0, evaluate("2+3*5"))
        assertEquals(25.0, evaluate("(2+3)*5"))
        assertEquals(11.0, evaluate("2*3+5"))
        assertEquals(40.0, evaluate("2*(3+5)*5/2"))
        assertEquals(8.0, evaluate("2^3"))
        assertEquals(16.0, evaluate("2^4"))
    }

    @Test
    fun `parse constants`() {
        assertEquals(Math.PI, evaluate("pi"), 1e-10)
        assertEquals(Math.E, evaluate("e"), 1e-10)
        assertEquals(Math.PI * 2, evaluate("2*pi"), 1e-10)
        assertEquals(Math.E * 3, evaluate("3*e"), 1e-10)
    }

    @Test
    fun `parse nested function calls`() {
        assertEquals(1.0, evaluate("sin(asin(1))"), 1e-10)
        assertEquals(2.0, evaluate("sqrt(sqrt(16))"), 1e-10)
        assertEquals(4.0, evaluate("sqrt(abs(-16))"), 1e-10)
        assertEquals(2.0, evaluate("log(exp(2))"), 1e-10)
    }

    @Test
    fun `parse mixed operators with factorial and percent`() {
        assertEquals(122.0, evaluate("5!+2")) // 120 + 2
        assertEquals(1.2, evaluate("5!%"), 1e-10) // (5!)% = 120% = 1.2
    }

    @Test
    fun `parse chained power with unary minus`() {
        assertEquals(-64.0, evaluate("-2^6")) // -(2^6) = -64
        assertEquals(-512.0, evaluate("-2^9")) // -(2^9) = -512
    }

    @Test
    fun `parse modulo operator`() {
        assertEquals(1.0, evaluate("10%3"))
        assertEquals(0.0, evaluate("10%5"))
        assertEquals(2.0, evaluate("17%5"))
    }

    @Test
    fun `parse negative numbers in parentheses`() {
        assertEquals(-5.0, evaluate("(-5)"))
        assertEquals(3.0, evaluate("(-1)+4"))
        assertEquals(-10.0, evaluate("(-2)*5"))
    }

    @Test
    fun `parse complex nested expressions`() {
        assertEquals(0.0, evaluate("sin(pi)"), 1e-10)
        assertEquals(1.0, evaluate("cos(0)"), 1e-10)
        assertEquals(Math.PI / 2, evaluate("asin(1)"), 1e-10)
        assertEquals(0.0, evaluate("log(1)"), 1e-10)
        assertEquals(1.0, evaluate("floor(1.9)"), 1e-10)
        assertEquals(2.0, evaluate("ceil(1.1)"), 1e-10)
        assertEquals(1.0, evaluate("round(1.4)"), 1e-10)
        assertEquals(2.0, evaluate("round(1.6)"), 1e-10)
    }

    @Test
    fun `parse scientific notation`() {
        assertEquals(1e10, evaluate("1e10"), 1e-5)
        assertEquals(1e-5, evaluate("1E-5"), 1e-10)
        assertEquals(314.0, evaluate("3.14e2"), 1e-5)
    }

    @Test
    fun `parse throws on mismatched parentheses`() {
        assertThrows(ParserException::class.java) { parse("(") }
        assertThrows(ParserException::class.java) { parse(")") }
        assertThrows(ParserException::class.java) { parse("(1+2") }
        assertThrows(ParserException::class.java) { parse("1+2)") }
    }

    @Test
    fun `parse throws on invalid syntax`() {
        assertThrows(ParserException::class.java) { parse("1 +") }
        assertThrows(ParserException::class.java) { parse("* 2") }
        // implicit multiplication now supports number next to number
        assertEquals(2.0, evaluate("1 2"))
    }
}