package com.calculator.core.engine

object Evaluator {

    fun evaluate(expr: Expr, angleMode: AngleMode = AngleMode.DEGREE): Result<Double> = try {
        Result.success(eval(expr, angleMode))
    } catch (e: ArithmeticException) {
        Result.failure(e)
    } catch (e: IllegalArgumentException) {
        Result.failure(e)
    } catch (e: EvaluationException) {
        Result.failure(e)
    }

    private fun eval(expr: Expr, angleMode: AngleMode): Double = when (expr) {
        is Expr.Number -> expr.value
        is Expr.Constant -> expr.constant.value
        is Expr.Binary -> evalBinary(expr, angleMode)
        is Expr.Unary -> evalUnary(expr, angleMode)
        is Expr.FunctionCall -> evalFunction(expr, angleMode)
        is Expr.Percent -> eval(expr.operand, angleMode) / 100.0
        is Expr.Factorial -> factorial(expr.operand, angleMode)
    }

    private fun evalBinary(expr: Expr.Binary, angleMode: AngleMode): Double {
        val left = eval(expr.left, angleMode)
        return when (expr.op) {
            Operator.ADD, Operator.SUB -> {
                val rightVal = when (expr.right) {
                    is Expr.Percent -> left * (eval(expr.right.operand, angleMode) / 100.0)
                    else -> eval(expr.right, angleMode)
                }
                if (expr.op == Operator.ADD) left + rightVal else left - rightVal
            }
            Operator.MUL -> {
                val rightVal = when (expr.right) {
                    is Expr.Percent -> eval(expr.right.operand, angleMode) / 100.0
                    else -> eval(expr.right, angleMode)
                }
                left * rightVal
            }
            Operator.DIV -> {
                val rightVal = when (expr.right) {
                    is Expr.Percent -> eval(expr.right.operand, angleMode) / 100.0
                    else -> eval(expr.right, angleMode)
                }
                if (rightVal == 0.0) throw ArithmeticException("Division by zero")
                left / rightVal
            }
            Operator.MOD -> left % eval(expr.right, angleMode)
            Operator.POW -> Math.pow(left, eval(expr.right, angleMode))
            else -> throw EvaluationException("Unsupported binary operator: ${expr.op}")
        }
    }

    private fun evalUnary(expr: Expr.Unary, angleMode: AngleMode): Double {
        val operand = eval(expr.operand, angleMode)
        return when (expr.op) {
            Operator.SUB -> -operand
            Operator.ADD -> operand
            else -> throw EvaluationException("Unsupported unary operator: ${expr.op}")
        }
    }

    private fun evalFunction(expr: Expr.FunctionCall, angleMode: AngleMode): Double {
        val args = expr.args.map { eval(it, angleMode) }
        return when (expr.fn) {
            Function.SIN -> Math.sin(toRadians(args[0], angleMode))
            Function.COS -> Math.cos(toRadians(args[0], angleMode))
            Function.TAN -> Math.tan(toRadians(args[0], angleMode))
            Function.ASIN -> {
                if (args[0] < -1 || args[0] > 1) throw IllegalArgumentException("asin domain error: argument must be in [-1, 1]")
                fromRadians(Math.asin(args[0]), angleMode)
            }
            Function.ACOS -> {
                if (args[0] < -1 || args[0] > 1) throw IllegalArgumentException("acos domain error: argument must be in [-1, 1]")
                fromRadians(Math.acos(args[0]), angleMode)
            }
            Function.ATAN -> fromRadians(Math.atan(args[0]), angleMode)
            Function.ASINH -> Math.log(args[0] + Math.sqrt(args[0] * args[0] + 1))
            Function.ACOSH -> {
                if (args[0] < 1) throw IllegalArgumentException("acosh domain error: argument must be >= 1")
                Math.log(args[0] + Math.sqrt(args[0] * args[0] - 1))
            }
            Function.ATANH -> {
                if (args[0] <= -1 || args[0] >= 1) throw IllegalArgumentException("atanh domain error: argument must be in (-1, 1)")
                0.5 * Math.log((1 + args[0]) / (1 - args[0]))
            }
            Function.SINH -> Math.sinh(args[0])
            Function.COSH -> Math.cosh(args[0])
            Function.TANH -> Math.tanh(args[0])
            Function.LOG -> {
                if (args[0] <= 0) throw IllegalArgumentException("log domain error: argument must be positive")
                Math.log(args[0])
            }
            Function.LN -> {
                if (args[0] <= 0) throw IllegalArgumentException("ln domain error: argument must be positive")
                Math.log(args[0])
            }
            Function.LOG10 -> {
                if (args[0] <= 0) throw IllegalArgumentException("log10 domain error: argument must be positive")
                Math.log10(args[0])
            }
            Function.SQRT -> {
                if (args[0] < 0) throw IllegalArgumentException("Square root of negative number")
                Math.sqrt(args[0])
            }
            Function.CBRT -> Math.cbrt(args[0])
            Function.EXP -> Math.exp(args[0])
            Function.ABS -> Math.abs(args[0])
            Function.FLOOR -> Math.floor(args[0])
            Function.CEIL -> Math.ceil(args[0])
            Function.ROUND -> Math.round(args[0]).toDouble()
            Function.DEG -> Math.toDegrees(args[0])
            Function.RAD -> Math.toRadians(args[0])
        }
    }

    private fun toRadians(value: Double, angleMode: AngleMode): Double {
        return when (angleMode) {
            AngleMode.DEGREE -> Math.toRadians(value)
            AngleMode.RADIAN -> value
        }
    }

    private fun fromRadians(value: Double, angleMode: AngleMode): Double {
        return when (angleMode) {
            AngleMode.DEGREE -> Math.toDegrees(value)
            AngleMode.RADIAN -> value
        }
    }

    private fun factorial(expr: Expr, angleMode: AngleMode): Double {
        val n = eval(expr, angleMode)
        if (n < 0 || n % 1 != 0.0) {
            throw IllegalArgumentException("Factorial only defined for non-negative integers")
        }
        if (n > 170) return Double.POSITIVE_INFINITY
        var result = 1.0
        for (i in 2..n.toInt()) {
            result *= i
        }
        return result
    }
}

class EvaluationException(message: String) : Exception(message)