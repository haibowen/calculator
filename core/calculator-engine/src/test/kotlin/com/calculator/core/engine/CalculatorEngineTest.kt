package com.calculator.core.engine

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class CalculatorEngineTest {

    private val engine = CalculatorEngine()

    @Test
    fun `evaluate simple addition`() {
        val result = engine.evaluate("1 + 2")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(3.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate subtraction`() {
        val result = engine.evaluate("5 - 3")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(2.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate multiplication`() {
        val result = engine.evaluate("4 * 3")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(12.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate division`() {
        val result = engine.evaluate("10 / 2")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(5.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate exponentiation`() {
        val result = engine.evaluate("2 ^ 3")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(8.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate modulo`() {
        val result = engine.evaluate("10 % 3")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate factorial`() {
        val result = engine.evaluate("5!")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(120.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate percent`() {
        val result = engine.evaluate("50%")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(0.5, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate with parentheses`() {
        val result = engine.evaluate("(1 + 2) * 3")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(9.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate unary minus`() {
        val result = engine.evaluate("-5")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(-5.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate unary plus`() {
        val result = engine.evaluate("+5")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(5.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate constants pi and e`() {
        val piResult = engine.evaluate("pi")
        assertTrue { piResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI, (piResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val eResult = engine.evaluate("e")
        assertTrue { eResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.E, (eResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate trigonometric functions`() {
        engine.angleMode = AngleMode.RADIAN
        val sinResult = engine.evaluate("sin(pi/2)")
        assertTrue { sinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (sinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val cosResult = engine.evaluate("cos(0)")
        assertTrue { cosResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (cosResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val tanResult = engine.evaluate("tan(pi/4)")
        assertTrue { tanResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (tanResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate inverse trigonometric functions`() {
        engine.angleMode = AngleMode.RADIAN
        val asinResult = engine.evaluate("asin(1)")
        assertTrue { asinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI / 2, (asinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val acosResult = engine.evaluate("acos(0)")
        assertTrue { acosResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI / 2, (acosResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val atanResult = engine.evaluate("atan(1)")
        assertTrue { atanResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI / 4, (atanResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate hyperbolic functions`() {
        val sinhResult = engine.evaluate("sinh(0)")
        assertTrue { sinhResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.0, (sinhResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val coshResult = engine.evaluate("cosh(0)")
        assertTrue { coshResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (coshResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate logarithmic functions`() {
        val logResult = engine.evaluate("log(e)")
        assertTrue { logResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (logResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val lnResult = engine.evaluate("ln(e)")
        assertTrue { lnResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (lnResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val log10Result = engine.evaluate("log10(100)")
        assertTrue { log10Result is CalculatorEngine.EngineResult.Success }
        assertEquals(2.0, (log10Result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate sqrt and cbrt`() {
        val sqrtResult = engine.evaluate("sqrt(16)")
        assertTrue { sqrtResult is CalculatorEngine.EngineResult.Success }
        assertEquals(4.0, (sqrtResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val cbrtResult = engine.evaluate("cbrt(27)")
        assertTrue { cbrtResult is CalculatorEngine.EngineResult.Success }
        assertEquals(3.0, (cbrtResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate exp and abs`() {
        val expResult = engine.evaluate("exp(1)")
        assertTrue { expResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.E, (expResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val absResult = engine.evaluate("abs(-5)")
        assertTrue { absResult is CalculatorEngine.EngineResult.Success }
        assertEquals(5.0, (absResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate floor ceil round`() {
        val floorResult = engine.evaluate("floor(3.7)")
        assertTrue { floorResult is CalculatorEngine.EngineResult.Success }
        assertEquals(3.0, (floorResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val ceilResult = engine.evaluate("ceil(3.2)")
        assertTrue { ceilResult is CalculatorEngine.EngineResult.Success }
        assertEquals(4.0, (ceilResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val roundResult = engine.evaluate("round(3.5)")
        assertTrue { roundResult is CalculatorEngine.EngineResult.Success }
        assertEquals(4.0, (roundResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate deg and rad`() {
        val degResult = engine.evaluate("deg(pi)")
        assertTrue { degResult is CalculatorEngine.EngineResult.Success }
        assertEquals(180.0, (degResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val radResult = engine.evaluate("rad(180)")
        assertTrue { radResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI, (radResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate deg or rad parity for trigonometric functions`() {
        // Test degree mode
        engine.angleMode = AngleMode.DEGREE
        val sinDeg = engine.evaluate("sin(90)")
        assertTrue { sinDeg is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (sinDeg as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val cosDeg = engine.evaluate("cos(180)")
        assertTrue { cosDeg is CalculatorEngine.EngineResult.Success }
        assertEquals(-1.0, (cosDeg as CalculatorEngine.EngineResult.Success).value, 1e-10)

        // Test radian mode
        engine.angleMode = AngleMode.RADIAN
        val sinRad = engine.evaluate("sin(pi/2)")
        assertTrue { sinRad is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (sinRad as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val cosRad = engine.evaluate("cos(pi)")
        assertTrue { cosRad is CalculatorEngine.EngineResult.Success }
        assertEquals(-1.0, (cosRad as CalculatorEngine.EngineResult.Success).value, 1e-10)

        // Verify parity: sin(90°) == sin(pi/2 rad)
        assertEquals((sinDeg as CalculatorEngine.EngineResult.Success).value, (sinRad as CalculatorEngine.EngineResult.Success).value, 1e-10)
        assertEquals((cosDeg as CalculatorEngine.EngineResult.Success).value, (cosRad as CalculatorEngine.EngineResult.Success).value, 1e-10)

        // Test inverse functions parity
        engine.angleMode = AngleMode.DEGREE
        val asinDeg = engine.evaluate("asin(1)")
        assertTrue { asinDeg is CalculatorEngine.EngineResult.Success }
        assertEquals(90.0, (asinDeg as CalculatorEngine.EngineResult.Success).value, 1e-10)

        engine.angleMode = AngleMode.RADIAN
        val asinRad = engine.evaluate("asin(1)")
        assertTrue { asinRad is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI / 2, (asinRad as CalculatorEngine.EngineResult.Success).value, 1e-10)

        assertEquals(Math.toDegrees((asinRad as CalculatorEngine.EngineResult.Success).value), (asinDeg as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate implicit multiplication`() {
        val result = engine.evaluate("2 pi")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(2.0 * Math.PI, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val result2 = engine.evaluate("(1+2)(3+4)")
        assertTrue { result2 is CalculatorEngine.EngineResult.Success }
        assertEquals(21.0, (result2 as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate scientific notation`() {
        val result = engine.evaluate("1e3 + 2e2")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(1200.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate complex expression`() {
        engine.angleMode = AngleMode.RADIAN
        val result = engine.evaluate("sin(pi/2) + cos(0) * 2")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(3.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate nested functions`() {
        val result = engine.evaluate("sqrt(sqrt(16))")
        assertTrue { result is CalculatorEngine.EngineResult.Success }
        assertEquals(2.0, (result as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `returns syntax error for invalid input`() {
        val result = engine.evaluate("1 + * 2")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
        assertEquals(CalculatorEngine.EngineError.SYNTAX_ERROR, (result as CalculatorEngine.EngineResult.Error).code)
    }

    @Test
    fun `returns math error for division by zero`() {
        val result = engine.evaluate("1 / 0")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
        assertEquals(CalculatorEngine.EngineError.MATH_ERROR, (result as CalculatorEngine.EngineResult.Error).code)
    }

    @Test
    fun `returns math error for sqrt of negative`() {
        val result = engine.evaluate("sqrt(-1)")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
        assertEquals(CalculatorEngine.EngineError.MATH_ERROR, (result as CalculatorEngine.EngineResult.Error).code)
    }

    @Test
    fun `returns math error for invalid factorial`() {
        val result = engine.evaluate("(-1)!")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
        assertEquals(CalculatorEngine.EngineError.MATH_ERROR, (result as CalculatorEngine.EngineResult.Error).code)
    }

    @Test
    fun `returns syntax error for unknown identifier`() {
        val result = engine.evaluate("foo(1)")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
        assertEquals(CalculatorEngine.EngineError.SYNTAX_ERROR, (result as CalculatorEngine.EngineResult.Error).code)
    }

    @Test
    fun `returns syntax error for empty input`() {
        val result = engine.evaluate("")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
    }

    @Test
    fun `returns syntax error for unmatched parenthesis`() {
        val result = engine.evaluate("(1 + 2")
        assertTrue { result is CalculatorEngine.EngineResult.Error }
        assertEquals(CalculatorEngine.EngineError.SYNTAX_ERROR, (result as CalculatorEngine.EngineResult.Error).code)
    }

    @Test
    fun `evaluate inverse mode trigonometric functions`() {
        engine.angleMode = AngleMode.DEGREE
        engine.inverseMode = false

        // Normal mode: sin(30) = 0.5
        val sinResult = engine.evaluate("sin(30)")
        assertTrue { sinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.5, (sinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val cosResult = engine.evaluate("cos(60)")
        assertTrue { cosResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.5, (cosResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val tanResult = engine.evaluate("tan(45)")
        assertTrue { tanResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (tanResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        // Inverse mode: inv sin(0.5) = 30
        engine.inverseMode = true
        val invSinResult = engine.evaluate("inv sin(0.5)")
        assertTrue { invSinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(30.0, (invSinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val invCosResult = engine.evaluate("inv cos(0.5)")
        assertTrue { invCosResult is CalculatorEngine.EngineResult.Success }
        assertEquals(60.0, (invCosResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val invTanResult = engine.evaluate("inv tan(1)")
        assertTrue { invTanResult is CalculatorEngine.EngineResult.Success }
        assertEquals(45.0, (invTanResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `evaluate inverse mode hyperbolic functions`() {
        engine.inverseMode = false

        // Normal mode
        val sinhResult = engine.evaluate("sinh(0)")
        assertTrue { sinhResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.0, (sinhResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val coshResult = engine.evaluate("cosh(0)")
        assertTrue { coshResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (coshResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        // Inverse mode: inv sinh(0) = 0, inv cosh(1) = 0
        engine.inverseMode = true
        val invSinhResult = engine.evaluate("inv sinh(0)")
        assertTrue { invSinhResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.0, (invSinhResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val invCoshResult = engine.evaluate("inv cosh(1)")
        assertTrue { invCoshResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.0, (invCoshResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val invTanhResult = engine.evaluate("inv tanh(0)")
        assertTrue { invTanhResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.0, (invTanhResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `inverse mode inverse functions map back to normal`() {
        engine.inverseMode = true
        engine.angleMode = AngleMode.DEGREE

        // In inverse mode, asin maps to sin
        val sinResult = engine.evaluate("inv asin(30)")
        assertTrue { sinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.5, (sinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val cosResult = engine.evaluate("inv acos(60)")
        assertTrue { cosResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.5, (cosResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val tanResult = engine.evaluate("inv atan(45)")
        assertTrue { tanResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (tanResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        // Inverse hyperbolic
        val sinhResult = engine.evaluate("inv asinh(0)")
        assertTrue { sinhResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.0, (sinhResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        val coshResult = engine.evaluate("inv acosh(0)")
        assertTrue { coshResult is CalculatorEngine.EngineResult.Success }
        assertEquals(1.0, (coshResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `inverse mode works in radian mode`() {
        engine.angleMode = AngleMode.RADIAN
        engine.inverseMode = false

        val sinResult = engine.evaluate("sin(pi/6)")
        assertTrue { sinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(0.5, (sinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)

        engine.inverseMode = true
        val invSinResult = engine.evaluate("inv sin(0.5)")
        assertTrue { invSinResult is CalculatorEngine.EngineResult.Success }
        assertEquals(Math.PI / 6, (invSinResult as CalculatorEngine.EngineResult.Success).value, 1e-10)
    }

    @Test
    fun `saveState and restoreState round trip`() {
        engine.angleMode = AngleMode.RADIAN
        engine.inverseMode = true
        engine.memoryStore(42.0)
        engine.evaluate("1 + 2")
        engine.evaluate("3 * 4")

        val json = engine.saveState()

        val newEngine = CalculatorEngine()
        newEngine.restoreState(json)

        assertEquals(AngleMode.RADIAN, newEngine.angleMode)
        assertEquals(true, newEngine.inverseMode)
        assertEquals(42.0, newEngine.memoryRecall(), 1e-10)
        assertEquals(2, newEngine.getHistory().size)
        assertEquals("1 + 2", newEngine.getHistory()[0].expression)
        assertEquals("3.0", newEngine.getHistory()[0].result)
        assertEquals("3 * 4", newEngine.getHistory()[1].expression)
        assertEquals("12.0", newEngine.getHistory()[1].result)
    }

    @Test
    fun `saveState with default values`() {
        val json = engine.saveState()
        val newEngine = CalculatorEngine()
        newEngine.restoreState(json)

        assertEquals(AngleMode.DEGREE, newEngine.angleMode)
        assertEquals(false, newEngine.inverseMode)
        assertEquals(0.0, newEngine.memoryRecall(), 1e-10)
        assertEquals(0, newEngine.getHistory().size)
    }

    @Test
    fun `memory operations work correctly`() {
        engine.memoryClear()
        assertEquals(0.0, engine.memoryRecall(), 1e-10)

        engine.memoryStore(10.0)
        assertEquals(10.0, engine.memoryRecall(), 1e-10)

        engine.memoryAdd(5.0)
        assertEquals(15.0, engine.memoryRecall(), 1e-10)

        engine.memorySubtract(3.0)
        assertEquals(12.0, engine.memoryRecall(), 1e-10)

        engine.memoryClear()
        assertEquals(0.0, engine.memoryRecall(), 1e-10)
    }

    @Test
    fun `history tracks evaluations`() {
        engine.clearHistory()
        assertEquals(0, engine.getHistory().size)

        engine.evaluate("1 + 1")
        engine.evaluate("2 * 2")

        val history = engine.getHistory()
        assertEquals(2, history.size)
        assertEquals("1 + 1", history[0].expression)
        assertEquals("2.0", history[0].result)
        assertEquals("2 * 2", history[1].expression)
        assertEquals("4.0", history[1].result)
    }

    @Test
    fun `history tracks errors`() {
        engine.clearHistory()

        engine.evaluate("1 + * 2")

        val history = engine.getHistory()
        assertEquals(1, history.size)
        assertEquals("1 + * 2", history[0].expression)
        assertTrue { history[0].result.startsWith("Error:") }
    }

    @Test
    fun `clearHistory clears history`() {
        engine.evaluate("1 + 1")
        engine.clearHistory()
        assertEquals(0, engine.getHistory().size)
    }
}