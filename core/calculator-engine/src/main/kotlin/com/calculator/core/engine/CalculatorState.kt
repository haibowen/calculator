package com.calculator.core.engine

import kotlinx.serialization.Serializable

@Serializable
data class CalculatorState(
    val angleMode: AngleMode = AngleMode.DEGREE,
    val inverseMode: Boolean = false,
    val memory: Double = 0.0,
    val history: List<HistoryEntry> = emptyList()
)