package com.calculator.core.engine

import kotlinx.serialization.Serializable

@Serializable
data class HistoryEntry(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)