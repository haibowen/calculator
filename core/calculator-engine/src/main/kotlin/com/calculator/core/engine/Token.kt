package com.calculator.core.engine

sealed interface Token {
    data class Number(val value: Double) : Token
    data class Operator(val op: com.calculator.core.engine.Operator) : Token
    data class Function(val fn: com.calculator.core.engine.Function) : Token
    data class Constant(val const: com.calculator.core.engine.MathConstant) : Token
    object LeftParen : Token
    object RightParen : Token
    object End : Token
}