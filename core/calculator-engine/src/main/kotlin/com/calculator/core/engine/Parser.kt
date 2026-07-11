package com.calculator.core.engine

class Parser(private val lexer: Lexer, inverseMode: Boolean = false) {

    private var current: Token = lexer.nextToken()

    fun parse(): Expr {
        val expr = parseExpression(0)
        if (current != Token.End) {
            throw ParserException("Unexpected token: $current")
        }
        return expr
    }

    private fun parseExpression(minPrec: Int): Expr {
        var left = parsePrimary()
        left = parsePostfix(left)

        while (true) {
            val token = current
            val (prec, assoc) = when (token) {
                is Token.Operator -> {
                    val op = token.op
                    // Skip postfix operators in binary operator parsing
                    if (op == Operator.FACTORIAL || op == Operator.PERCENT) break
                    precedence(op)
                }
                else -> break
            }
            if (prec < minPrec) break

            val op = token.op
            advance()

            val nextMinPrec = if (assoc == Operator.Associativity.RIGHT) prec else prec + 1
            var right = parseExpression(nextMinPrec)
            right = parsePostfix(right)
            left = Expr.Binary(left, op, right)
        }

        return left
    }

    private fun parsePrimary(): Expr {
        val token = current
        return when (token) {
            is Token.Number -> {
                advance()
                Expr.Number(token.value)
            }
            is Token.Constant -> {
                val cnst = token.const
                advance()
                Expr.Constant(cnst)
            }
            is Token.Function -> {
                val fn = token.fn
                advance()
                parseFunctionCall(fn)
            }
            Token.LeftParen -> {
                advance()
                val expr = parseExpression(0)
                expect(Token.RightParen)
                advance()
                expr
            }
            is Token.Operator -> when (token.op) {
                Operator.SUB -> {
                    advance()
                    val operand = parseExpression(precedence(Operator.SUB).first)
                    Expr.Unary(Operator.SUB, operand)
                }
                Operator.ADD -> {
                    advance()
                    parseExpression(precedence(Operator.ADD).first)
                }
                else -> throw ParserException("Unexpected operator: ${token.op}")
            }
            Token.End -> throw ParserException("Unexpected end of input")
            else -> throw ParserException("Unexpected token: $token")
        }
    }

    private fun parseFunctionCall(fn: Function): Expr {
        expect(Token.LeftParen)
        advance()

        val args = mutableListOf<Expr>()
        if (current != Token.RightParen) {
            while (true) {
                args.add(parseExpression(0))
                if (current == Token.RightParen) break
                throw ParserException("Multiple arguments not supported (comma not implemented)")
            }
        }
        expect(Token.RightParen)
        advance()
        return Expr.FunctionCall(fn, args)
    }

    private fun parsePostfix(expr: Expr): Expr {
        var result = expr
        while (true) {
            val token = current
            if (token is Token.Operator) {
                val op = token.op
                when (op) {
                    Operator.FACTORIAL -> {
                        advance()
                        result = Expr.Factorial(result)
                    }
                    Operator.MOD -> {
                        // Distinguish between modulo (infix) and percent (postfix)
                        // Look ahead: if next token can start a primary expression, it's modulo
                        val nextToken = lexer.peekNextToken()
                        val canStartPrimary = when (nextToken) {
                            is Token.Number, is Token.Constant, is Token.Function, Token.LeftParen -> true
                            is Token.Operator -> nextToken.op == Operator.SUB || nextToken.op == Operator.ADD
                            else -> false
                        }
                        if (canStartPrimary) {
                            // It's modulo (infix), let the binary operator loop handle it
                            return result
                        } else {
                            // It's percent (postfix)
                            advance()
                            result = Expr.Percent(result)
                        }
                    }
                    else -> return result
                }
            } else if (token == Token.LeftParen || token is Token.Function || token is Token.Number || token is Token.Constant) {
                // Implicit multiplication: 2(3+4) or 2 pi or (1+2)(3+4)
                val right = parsePrimary()
                result = Expr.Binary(result, Operator.MUL, right)
            } else {
                return result
            }
        }
    }

    private fun precedence(op: Operator): Pair<Int, Operator.Associativity> {
        return Pair(op.precedence, op.associativity)
    }

    private fun advance() {
        current = lexer.nextToken()
    }

    private fun expect(token: Token) {
        if (current != token) {
            throw ParserException("Expected $token but found $current")
        }
    }
}

class ParserException(message: String) : Exception(message)