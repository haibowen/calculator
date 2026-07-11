package com.calculator.core.engine

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class LexerTest {

    private fun tokenize(input: String): List<Token> {
        return Lexer(input).tokenizeAll()
    }

    private fun assertTokens(input: String, expected: List<Token>) {
        val tokens = tokenize(input)
        assertEquals(expected, tokens, { "Input: $input" })
    }

    @Test
    fun `tokenize integers`() {
        assertTokens("42", listOf(Token.Number(42.0), Token.End))
        assertTokens("0", listOf(Token.Number(0.0), Token.End))
        assertTokens("123456", listOf(Token.Number(123456.0), Token.End))
    }

    @Test
    fun `tokenize decimals`() {
        assertTokens("3.14", listOf(Token.Number(3.14), Token.End))
        assertTokens(".5", listOf(Token.Number(0.5), Token.End))
        assertTokens("2.", listOf(Token.Number(2.0), Token.End))
        assertTokens("0.001", listOf(Token.Number(0.001), Token.End))
    }

    @Test
    fun `tokenize scientific notation`() {
        assertTokens("1e10", listOf(Token.Number(1e10), Token.End))
        assertTokens("1E-5", listOf(Token.Number(1e-5), Token.End))
        assertTokens("3.14e+2", listOf(Token.Number(3.14e2), Token.End))
        assertTokens("6.022e23", listOf(Token.Number(6.022e23), Token.End))
    }

    @Test
    fun `tokenize basic operators`() {
        assertTokens("+", listOf(Token.Operator(Operator.ADD), Token.End))
        assertTokens("-", listOf(Token.Operator(Operator.SUB), Token.End))
        assertTokens("*", listOf(Token.Operator(Operator.MUL), Token.End))
        assertTokens("/", listOf(Token.Operator(Operator.DIV), Token.End))
        assertTokens("%", listOf(Token.Operator(Operator.MOD), Token.End))
        assertTokens("^", listOf(Token.Operator(Operator.POW), Token.End))
        assertTokens("!", listOf(Token.Operator(Operator.FACTORIAL), Token.End))
    }

    @Test
    fun `tokenize parentheses`() {
        assertTokens("(", listOf(Token.LeftParen, Token.End))
        assertTokens(")", listOf(Token.RightParen, Token.End))
        assertTokens("(1+2)", listOf(
            Token.LeftParen,
            Token.Number(1.0),
            Token.Operator(Operator.ADD),
            Token.Number(2.0),
            Token.RightParen,
            Token.End
        ))
    }

    @Test
    fun `tokenize constants`() {
        assertTokens("pi", listOf(Token.Constant(MathConstant.PI), Token.End))
        assertTokens("PI", listOf(Token.Constant(MathConstant.PI), Token.End))
        assertTokens("e", listOf(Token.Constant(MathConstant.E), Token.End))
        assertTokens("E", listOf(Token.Constant(MathConstant.E), Token.End))
    }

    @Test
    fun `tokenize functions`() {
        assertTokens("sin(0)", listOf(
            Token.Function(Function.SIN),
            Token.LeftParen,
            Token.Number(0.0),
            Token.RightParen,
            Token.End
        ))
        assertTokens("cos(PI)", listOf(
            Token.Function(Function.COS),
            Token.LeftParen,
            Token.Constant(MathConstant.PI),
            Token.RightParen,
            Token.End
        ))
        assertTokens("sqrt(4)", listOf(
            Token.Function(Function.SQRT),
            Token.LeftParen,
            Token.Number(4.0),
            Token.RightParen,
            Token.End
        ))
        assertTokens("log10(100)", listOf(
            Token.Function(Function.LOG10),
            Token.LeftParen,
            Token.Number(100.0),
            Token.RightParen,
            Token.End
        ))
        assertTokens("SIN(0)", listOf(
            Token.Function(Function.SIN),
            Token.LeftParen,
            Token.Number(0.0),
            Token.RightParen,
            Token.End
        ))
    }

    @Test
    fun `tokenize complex expressions`() {
        assertTokens("1 + 2 * 3", listOf(
            Token.Number(1.0),
            Token.Operator(Operator.ADD),
            Token.Number(2.0),
            Token.Operator(Operator.MUL),
            Token.Number(3.0),
            Token.End
        ))

        assertTokens("2 * (3 + 4)", listOf(
            Token.Number(2.0),
            Token.Operator(Operator.MUL),
            Token.LeftParen,
            Token.Number(3.0),
            Token.Operator(Operator.ADD),
            Token.Number(4.0),
            Token.RightParen,
            Token.End
        ))

        assertTokens("sin(pi/2) + cos(0)", listOf(
            Token.Function(Function.SIN),
            Token.LeftParen,
            Token.Constant(MathConstant.PI),
            Token.Operator(Operator.DIV),
            Token.Number(2.0),
            Token.RightParen,
            Token.Operator(Operator.ADD),
            Token.Function(Function.COS),
            Token.LeftParen,
            Token.Number(0.0),
            Token.RightParen,
            Token.End
        ))
    }

    @Test
    fun `tokenize negative numbers`() {
        assertTokens("-5", listOf(
            Token.Operator(Operator.SUB),
            Token.Number(5.0),
            Token.End
        ))
        assertTokens("3 + -2", listOf(
            Token.Number(3.0),
            Token.Operator(Operator.ADD),
            Token.Operator(Operator.SUB),
            Token.Number(2.0),
            Token.End
        ))
        assertTokens("-(2 + 3)", listOf(
            Token.Operator(Operator.SUB),
            Token.LeftParen,
            Token.Number(2.0),
            Token.Operator(Operator.ADD),
            Token.Number(3.0),
            Token.RightParen,
            Token.End
        ))
    }

    @Test
    fun `tokenize whitespace handling`() {
        assertTokens("  1  +  2  ", listOf(
            Token.Number(1.0),
            Token.Operator(Operator.ADD),
            Token.Number(2.0),
            Token.End
        ))
        assertTokens("\t3\n*\r4", listOf(
            Token.Number(3.0),
            Token.Operator(Operator.MUL),
            Token.Number(4.0),
            Token.End
        ))
        assertTokens("", listOf(Token.End))
        assertTokens("   ", listOf(Token.End))
    }

    @Test
    fun `tokenize factorial and percent`() {
        assertTokens("5!", listOf(
            Token.Number(5.0),
            Token.Operator(Operator.FACTORIAL),
            Token.End
        ))
        assertTokens("50%", listOf(
            Token.Number(50.0),
            Token.Operator(Operator.MOD),
            Token.End
        ))
    }

    @Test
    fun `tokenize nested functions`() {
        assertTokens("sqrt(abs(-4))", listOf(
            Token.Function(Function.SQRT),
            Token.LeftParen,
            Token.Function(Function.ABS),
            Token.LeftParen,
            Token.Operator(Operator.SUB),
            Token.Number(4.0),
            Token.RightParen,
            Token.RightParen,
            Token.End
        ))
    }

    @Test
    fun `tokenize throws on unknown identifier`() {
        assertThrows(LexerException::class.java) { tokenize("foo") }
        assertThrows(LexerException::class.java) { tokenize("unknown(1)") }
        assertThrows(LexerException::class.java) { tokenize("sinx") }
    }

    @Test
    fun `tokenize throws on unknown operator`() {
        assertThrows(LexerException::class.java) { tokenize("@") }
        assertThrows(LexerException::class.java) { tokenize("#") }
        assertThrows(LexerException::class.java) { tokenize("$") }
    }

    @Test
    fun `tokenize all trigonometric functions`() {
        val trigFunctions = listOf(
            "sin" to Function.SIN,
            "cos" to Function.COS,
            "tan" to Function.TAN,
            "asin" to Function.ASIN,
            "acos" to Function.ACOS,
            "atan" to Function.ATAN,
            "sinh" to Function.SINH,
            "cosh" to Function.COSH,
            "tanh" to Function.TANH
        )

        for ((name, fn) in trigFunctions) {
            val tokens = tokenize("$name(0)")
            assertEquals(Token.Function(fn), tokens[0], { "Function: $name" })
            assertEquals(Token.LeftParen, tokens[1])
            assertEquals(Token.Number(0.0), tokens[2])
            assertEquals(Token.RightParen, tokens[3])
            assertEquals(Token.End, tokens[4])
        }
    }

    @Test
    fun `tokenize all logarithmic functions`() {
        val logFunctions = listOf(
            "log" to Function.LOG,
            "ln" to Function.LN,
            "log10" to Function.LOG10
        )

        for ((name, fn) in logFunctions) {
            val tokens = tokenize("$name(10)")
            assertEquals(Token.Function(fn), tokens[0], { "Function: $name" })
        }
    }

    @Test
    fun `tokenize power function`() {
        assertTokens("2^3", listOf(
            Token.Number(2.0),
            Token.Operator(Operator.POW),
            Token.Number(3.0),
            Token.End
        ))
        assertTokens("2 ^ 10", listOf(
            Token.Number(2.0),
            Token.Operator(Operator.POW),
            Token.Number(10.0),
            Token.End
        ))
    }

    @Test
    fun `tokenize chained operators`() {
        assertTokens("1+2-3*4/5", listOf(
            Token.Number(1.0),
            Token.Operator(Operator.ADD),
            Token.Number(2.0),
            Token.Operator(Operator.SUB),
            Token.Number(3.0),
            Token.Operator(Operator.MUL),
            Token.Number(4.0),
            Token.Operator(Operator.DIV),
            Token.Number(5.0),
            Token.End
        ))
    }
}