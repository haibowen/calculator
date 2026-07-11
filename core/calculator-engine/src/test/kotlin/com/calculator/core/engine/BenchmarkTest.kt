package com.calculator.core.engine

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTimedValue

class BenchmarkTest {

    private val engine = CalculatorEngine()
    private val random = Random.Default

    @Test
    fun `evaluate 10000 expressions less than 500ms`() {
        val expressions = List(10000) {
            "sin(${random.nextDouble() * Math.PI}) + cos(${random.nextDouble() * Math.PI}) * ${random.nextInt(100)}"
        }
        val result = measureTimedValue { expressions.forEach { engine.evaluate(it) } }
        assertTrue(result.duration < 500.milliseconds)
    }
}
