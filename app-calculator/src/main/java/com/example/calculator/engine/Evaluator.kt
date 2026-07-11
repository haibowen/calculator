package com.example.calculator.engine

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.*

object Evaluator {
    private val MC = MathContext.DECIMAL128
    private val BD_PI = BigDecimal("3.14159265358979323846264338327950288419")
    private val BD_E = BigDecimal("2.71828182845904523536028747135266249775")

    var isDegreeMode = true

    data class Result(val value: BigDecimal?, val error: String?)

    sealed class Expr {
        data class Num(val v: BigDecimal) : Expr()
        data class UnaryOp(val op: Token, val expr: Expr) : Expr()
        data class BinOp(val left: Expr, val op: Token, val right: Expr) : Expr()
        data class Func(val name: String, val arg: Expr) : Expr()
        data class Var(val name: String) : Expr()
    }

    fun evaluate(tokens: List<Token>): Result {
        return try {
            val parser = Parser(tokens)
            val ast = parser.parseExpression()
            val value = evalAst(ast)
            Result(value, null)
        } catch (e: ArithmeticException) {
            Result(null, "除数不能为零")
        } catch (e: IllegalArgumentException) {
            Result(null, e.message ?: "表达式错误")
        } catch (e: Exception) {
            Result(null, "表达式错误")
        }
    }

    private fun evalAst(expr: Expr): BigDecimal {
        return when (expr) {
            is Expr.Num -> expr.v
            is Expr.UnaryOp -> {
                val v = evalAst(expr.expr)
                when (expr.op) {
                    is Token.Negate -> v.negate()
                    is Token.Factorial -> factorial(v)
                    is Token.Percent -> v.divide(BigDecimal(100), MC)
                    else -> throw IllegalArgumentException("未知运算符")
                }
            }
            is Expr.BinOp -> {
                val l = evalAst(expr.left)
                val r = evalAst(expr.right)
                when (expr.op) {
                    is Token.Plus -> l.add(r, MC)
                    is Token.Minus -> l.subtract(r, MC)
                    is Token.Multiply -> l.multiply(r, MC)
                    is Token.Divide -> {
                        if (r.compareTo(BigDecimal.ZERO) == 0) throw ArithmeticException()
                        l.divide(r, MC)
                    }
                    is Token.Power -> pow(l, r)
                    else -> throw IllegalArgumentException("未知运算符")
                }
            }
            is Expr.Func -> {
                val arg = evalAst(expr.arg)
                when (expr.name) {
                    "sin" -> BigDecimal(sin(toRadiansOrDegrees(arg).toDouble())).round(MC)
                    "cos" -> BigDecimal(cos(toRadiansOrDegrees(arg).toDouble())).round(MC)
                    "tan" -> BigDecimal(tan(toRadiansOrDegrees(arg).toDouble())).round(MC)
                    "asin" -> BigDecimal(asin(arg.toDouble())).round(MC)
                    "acos" -> BigDecimal(acos(arg.toDouble())).round(MC)
                    "atan" -> BigDecimal(atan(arg.toDouble())).round(MC)
                    "log" -> BigDecimal(log10(arg.toDouble())).round(MC)
                    "ln" -> BigDecimal(ln(arg.toDouble())).round(MC)
                    "sqrt" -> BigDecimal(sqrt(arg.toDouble())).round(MC)
                    "cbrt" -> BigDecimal(cbrt(arg.toDouble())).round(MC)
                    "square" -> arg.multiply(arg, MC)
                    "cube" -> arg.multiply(arg, MC).multiply(arg, MC)
                    else -> throw IllegalArgumentException("未知函数: ${expr.name}")
                }
            }
            is Expr.Var -> when (expr.name) {
                "pi" -> BD_PI; "e" -> BD_E
                else -> throw IllegalArgumentException("未知常量: ${expr.name}")
            }
        }
    }

    private fun toRadiansOrDegrees(v: BigDecimal): BigDecimal {
        return if (isDegreeMode) BigDecimal(Math.toRadians(v.toDouble())) else v
    }

    private fun factorial(n: BigDecimal): BigDecimal {
        val intVal = n.toInt()
        if (intVal < 0 || intVal > 170) throw IllegalArgumentException("阶乘参数超出范围")
        var result = BigDecimal.ONE
        for (i in 2..intVal) result = result.multiply(BigDecimal(i))
        return result
    }

    private fun pow(base: BigDecimal, exp: BigDecimal): BigDecimal {
        return try {
            if (exp.scale() <= 0) {
                base.pow(exp.toInt(), MC)
            } else {
                BigDecimal(Math.pow(base.toDouble(), exp.toDouble()), MC)
            }
        } catch (e: Exception) {
            BigDecimal(Math.pow(base.toDouble(), exp.toDouble()), MC)
        }
    }

    private class Parser(private val tokens: List<Token>) {
        private var pos = 0
        private fun peek() = tokens[pos]
        private fun consume() = tokens[pos++]

        fun parseExpression(): Expr {
            var left = parseTerm()
            while (peek() is Token.Plus || peek() is Token.Minus) {
                val op = consume()
                val right = parseTerm()
                left = Expr.BinOp(left, op, right)
            }
            return left
        }

        private fun parseTerm(): Expr {
            var left = parseFactor()
            while (peek() is Token.Multiply || peek() is Token.Divide) {
                val op = consume()
                val right = parseFactor()
                left = Expr.BinOp(left, op, right)
            }
            return left
        }

        private fun parseFactor(): Expr {
            if (peek() is Token.Negate) {
                consume()
                val expr = parseFactor()
                return Expr.UnaryOp(Token.Negate, expr)
            }
            var left = parsePower()
            while (peek() is Token.Power) {
                consume()
                val right = parseFactor()
                left = Expr.BinOp(left, Token.Power, right)
            }
            return left
        }

        private fun parsePower(): Expr {
            var expr = parsePrimary()
            while (peek() is Token.Factorial || peek() is Token.Percent) {
                val op = consume()
                expr = Expr.UnaryOp(op, expr)
            }
            return expr
        }

        private fun parsePrimary(): Expr {
            return when (val t = peek()) {
                is Token.Number -> { consume(); Expr.Num(t.value) }
                is Token.LParen -> {
                    consume()
                    val expr = parseExpression()
                    if (peek() is Token.RParen) consume()
                    expr
                }
                is Token.Pi -> { consume(); Expr.Var("pi") }
                is Token.E -> { consume(); Expr.Var("e") }
                is Token.Sin -> { consume(); Expr.Func("sin", parsePrimary()) }
                is Token.Cos -> { consume(); Expr.Func("cos", parsePrimary()) }
                is Token.Tan -> { consume(); Expr.Func("tan", parsePrimary()) }
                is Token.Asin -> { consume(); Expr.Func("asin", parsePrimary()) }
                is Token.Acos -> { consume(); Expr.Func("acos", parsePrimary()) }
                is Token.Atan -> { consume(); Expr.Func("atan", parsePrimary()) }
                is Token.Log -> { consume(); Expr.Func("log", parsePrimary()) }
                is Token.Ln -> { consume(); Expr.Func("ln", parsePrimary()) }
                is Token.Sqrt -> { consume(); Expr.Func("sqrt", parsePrimary()) }
                is Token.Cbrt -> { consume(); Expr.Func("cbrt", parsePrimary()) }
                is Token.Square -> { consume(); Expr.Func("square", parsePrimary()) }
                is Token.Cube -> { consume(); Expr.Func("cube", parsePrimary()) }
                else -> throw IllegalArgumentException("意外的符号: $t")
            }
        }
    }
}
