package com.calculator.core.engine

class Lexer(private val input: String, private val inverseMode: Boolean = false) {
    private var pos = 0
    private val chars = input.toCharArray()

    fun nextToken(): Token = when {
        pos >= chars.size -> Token.End
        chars[pos].isWhitespace() -> { skipWhitespace(); nextToken() }
        chars[pos].isDigit() || chars[pos] == '.' -> readNumber()
        chars[pos].isLetter() -> readIdentifier()
        else -> readOperator()
    }

    fun peekNextToken(): Token {
        val savedPos = pos
        val token = nextToken()
        pos = savedPos
        return token
    }

    fun tokenizeAll(): List<Token> {
        val tokens = mutableListOf<Token>()
        var token = nextToken()
        while (token != Token.End) {
            tokens.add(token)
            token = nextToken()
        }
        tokens.add(Token.End)
        return tokens
    }

    private fun skipWhitespace() {
        while (pos < chars.size && chars[pos].isWhitespace()) pos++
    }

    private fun readNumber(): Token.Number {
        val start = pos
        var hasDot = false
        var hasExp = false

        while (pos < chars.size) {
            val c = chars[pos]
            if (c.isDigit()) {
                pos++
            } else if (c == '.' && !hasDot && !hasExp) {
                hasDot = true
                pos++
            } else if ((c == 'e' || c == 'E') && !hasExp && pos > start) {
                val lookahead = pos + 1
                if (lookahead < chars.size) {
                    val nextChar = chars[lookahead]
                    if (nextChar.isDigit() || nextChar == '+' || nextChar == '-') {
                        hasExp = true
                        pos++
                        if (pos < chars.size && (chars[pos] == '+' || chars[pos] == '-')) {
                            pos++
                        }
                    } else {
                        break
                    }
                } else {
                    break
                }
            } else {
                break
            }
        }

        val numStr = String(chars, start, pos - start)
        return Token.Number(numStr.toDouble())
    }

    private fun readIdentifier(): Token {
        val start = pos
        while (pos < chars.size && (chars[pos].isLetterOrDigit() || chars[pos] == '_')) {
            pos++
        }
        val name = String(chars, start, pos - start).lowercase()

        // Handle "inv" prefix for inverse functions when inverseMode is enabled
        if (inverseMode && name == "inv") {
            // Skip whitespace after "inv"
            val savedPos = pos
            while (pos < chars.size && chars[pos].isWhitespace()) pos++
            // Check if next chars form a function name
            val funcStart = pos
            while (pos < chars.size && (chars[pos].isLetterOrDigit() || chars[pos] == '_')) {
                pos++
            }
            if (pos > funcStart) {
                val funcName = String(chars, funcStart, pos - funcStart).lowercase()
                val inverseFunc = when (funcName) {
                    "sin" -> Function.ASIN
                    "cos" -> Function.ACOS
                    "tan" -> Function.ATAN
                    "asin" -> Function.SIN
                    "acos" -> Function.COS
                    "atan" -> Function.TAN
                    "sinh" -> Function.ASINH
                    "cosh" -> Function.ACOSH
                    "tanh" -> Function.ATANH
                    "asinh" -> Function.SINH
                    "acosh" -> Function.COSH
                    "atanh" -> Function.TANH
                    else -> null
                }
                if (inverseFunc != null) {
                    return Token.Function(inverseFunc)
                }
            }
            // If not a valid function after "inv", restore position and fall through to error
            pos = savedPos
        }

        // Handle compound names like "invsinh" (without space)
        if (inverseMode && name.startsWith("inv") && name.length > 3) {
            val funcName = name.substring(3)
            val inverseFunc = when (funcName) {
                "sin" -> Function.ASIN
                "cos" -> Function.ACOS
                "tan" -> Function.ATAN
                "asin" -> Function.SIN
                "acos" -> Function.COS
                "atan" -> Function.TAN
                "sinh" -> Function.ASINH
                "cosh" -> Function.ACOSH
                "tanh" -> Function.ATANH
                "asinh" -> Function.SINH
                "acosh" -> Function.COSH
                "atanh" -> Function.TANH
                else -> null
            }
            if (inverseFunc != null) {
                return Token.Function(inverseFunc)
            }
        }

        return when (name) {
            "pi" -> Token.Constant(MathConstant.PI)
            "e" -> Token.Constant(MathConstant.E)
            "sin" -> Token.Function(Function.SIN)
            "cos" -> Token.Function(Function.COS)
            "tan" -> Token.Function(Function.TAN)
            "asin" -> Token.Function(Function.ASIN)
            "acos" -> Token.Function(Function.ACOS)
            "atan" -> Token.Function(Function.ATAN)
            "asinh" -> Token.Function(Function.ASINH)
            "acosh" -> Token.Function(Function.ACOSH)
            "atanh" -> Token.Function(Function.ATANH)
            "sinh" -> Token.Function(Function.SINH)
            "cosh" -> Token.Function(Function.COSH)
            "tanh" -> Token.Function(Function.TANH)
            "log" -> Token.Function(Function.LOG)
            "ln" -> Token.Function(Function.LN)
            "log10" -> Token.Function(Function.LOG10)
            "sqrt" -> Token.Function(Function.SQRT)
            "cbrt" -> Token.Function(Function.CBRT)
            "exp" -> Token.Function(Function.EXP)
            "abs" -> Token.Function(Function.ABS)
            "floor" -> Token.Function(Function.FLOOR)
            "ceil" -> Token.Function(Function.CEIL)
            "round" -> Token.Function(Function.ROUND)
            "deg" -> Token.Function(Function.DEG)
            "rad" -> Token.Function(Function.RAD)
            else -> throw LexerException("Unknown identifier: $name")
        }
    }

    private fun readOperator(): Token {
        return when (chars[pos++]) {
            '+' -> Token.Operator(Operator.ADD)
            '-' -> Token.Operator(Operator.SUB)
            '*' -> Token.Operator(Operator.MUL)
            '/' -> Token.Operator(Operator.DIV)
            '%' -> Token.Operator(Operator.MOD)
            '^' -> Token.Operator(Operator.POW)
            '!' -> Token.Operator(Operator.FACTORIAL)
            '(' -> Token.LeftParen
            ')' -> Token.RightParen
            else -> throw LexerException("Unknown operator: ${chars[pos - 1]}")
        }
    }
}

class LexerException(message: String) : Exception(message)