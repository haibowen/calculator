package com.calculator.core.engine

import com.calculator.core.engine.Operator
import com.calculator.core.engine.Function
import com.calculator.core.engine.MathConstant

sealed interface Expr {
    data class Number(val value: Double) : Expr
    data class Binary(val left: Expr, val op: Operator, val right: Expr) : Expr
    data class Unary(val op: Operator, val operand: Expr) : Expr
    data class FunctionCall(val fn: Function, val args: List<Expr>) : Expr
    data class Constant(val constant: MathConstant) : Expr
    data class Percent(val operand: Expr) : Expr
    data class Factorial(val operand: Expr) : Expr
}