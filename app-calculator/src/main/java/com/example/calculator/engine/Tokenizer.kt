package com.example.calculator.engine

class Tokenizer(private val input: String) {
    private var pos = 0

    fun tokenize(): List<Token> {
        pos = 0
        val tokens = mutableListOf<Token>()
        while (pos < input.length) {
            val ch = input[pos]
            when {
                ch.isWhitespace() -> pos++
                ch == '+' -> { tokens.add(Token.Plus); pos++ }
                ch == '-' -> {
                    val prev = tokens.lastOrNull()
                    if (prev == null || prev is Token.LParen || prev is Token.Multiply || prev is Token.Divide || prev is Token.Plus || prev is Token.Minus || prev is Token.Power || prev is Token.Negate || prev is Token.Percent || prev is Token.Factorial) {
                        tokens.add(Token.Negate)
                    } else {
                        tokens.add(Token.Minus)
                    }
                    pos++
                }
                ch == '×' || ch == '*' -> { tokens.add(Token.Multiply); pos++ }
                ch == '÷' || ch == '/' -> { tokens.add(Token.Divide); pos++ }
                ch == '^' -> { tokens.add(Token.Power); pos++ }
                ch == '%' -> { tokens.add(Token.Percent); pos++ }
                ch == '!' -> { tokens.add(Token.Factorial); pos++ }
                ch == '(' -> { tokens.add(Token.LParen); pos++ }
                ch == ')' -> { tokens.add(Token.RParen); pos++ }
                ch == 'π' -> { tokens.add(Token.Pi); pos++ }
                ch.isDigit() || ch == '.' -> parseNumber(tokens)
                ch.isLetter() -> parseFunctionOrConstant(tokens)
                else -> pos++
            }
        }
        tokens.add(Token.End)
        return tokens
    }

    private fun parseNumber(tokens: MutableList<Token>) {
        val start = pos
        while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) pos++
        tokens.add(Token.Number(java.math.BigDecimal(input.substring(start, pos))))
    }

    private fun parseFunctionOrConstant(tokens: MutableList<Token>) {
        val start = pos
        while (pos < input.length && input[pos].isLetter()) pos++
        val name = input.substring(start, pos).lowercase()
        tokens.add(when (name) {
            "sin" -> Token.Sin; "cos" -> Token.Cos; "tan" -> Token.Tan
            "asin" -> Token.Asin; "acos" -> Token.Acos; "atan" -> Token.Atan
            "log" -> Token.Log; "ln" -> Token.Ln
            "sqrt" -> Token.Sqrt; "cbrt" -> Token.Cbrt
            "square" -> Token.Square; "cube" -> Token.Cube
            "pi" -> Token.Pi; "e" -> Token.E
            else -> throw IllegalArgumentException("未知函数: $name")
        })
    }
}
