package com.calculator.core.engine

enum class Operator(val precedence: Int, val associativity: Associativity) {
    ADD(1, Associativity.LEFT), SUB(1, Associativity.LEFT),
    MUL(2, Associativity.LEFT), DIV(2, Associativity.LEFT),
    MOD(2, Associativity.LEFT),
    POW(3, Associativity.RIGHT),
    FACTORIAL(4, Associativity.LEFT),
    PERCENT(4, Associativity.LEFT);

    enum class Associativity { LEFT, RIGHT }
}

enum class Function(val argCount: Int) {
    SIN(1), COS(1), TAN(1),
    ASIN(1), ACOS(1), ATAN(1),
    ASINH(1), ACOSH(1), ATANH(1),
    SINH(1), COSH(1), TANH(1),
    LOG(1), LN(1), LOG10(1),
    SQRT(1), CBRT(1),
    EXP(1),
    ABS(1), FLOOR(1), CEIL(1), ROUND(1),
    DEG(1), RAD(1);
}

enum class MathConstant(val value: Double) {
    PI(Math.PI), E(Math.E);
}