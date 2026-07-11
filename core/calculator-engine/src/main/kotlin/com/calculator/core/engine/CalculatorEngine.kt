package com.calculator.core.engine

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CalculatorEngine {
    var angleMode: AngleMode = AngleMode.DEGREE
    var inverseMode = false
    var memory: Double = 0.0
    private val history = mutableListOf<HistoryEntry>()

    fun evaluate(input: String): EngineResult {
        val result = try {
            val tokens = Lexer(input, inverseMode).tokenizeAll()
            val ast = Parser(Lexer(input, inverseMode), inverseMode).parse()
            val value = Evaluator.evaluate(ast, angleMode).getOrThrow()
            EngineResult.Success(value)
        } catch (e: ParserException) {
            EngineResult.Error(EngineError.SYNTAX_ERROR, e.message)
        } catch (e: ArithmeticException) {
            EngineResult.Error(EngineError.MATH_ERROR, e.message)
        } catch (e: IllegalArgumentException) {
            EngineResult.Error(EngineError.MATH_ERROR, e.message)
        } catch (e: EvaluationException) {
            EngineResult.Error(EngineError.MATH_ERROR, e.message)
        } catch (e: LexerException) {
            EngineResult.Error(EngineError.SYNTAX_ERROR, e.message)
        } catch (e: Exception) {
            EngineResult.Error(EngineError.INVALID_INPUT, e.message)
        }
        addToHistory(input, result)
        return result
    }

    fun addToHistory(expression: String, result: EngineResult) {
        val resultString = when (result) {
            is EngineResult.Success -> result.value.toString()
            is EngineResult.Error -> "Error: ${result.code}"
        }
        history.add(HistoryEntry(expression, resultString))
    }

    fun getHistory(): List<HistoryEntry> = history.toList()

    fun clearHistory() {
        history.clear()
    }

    fun memoryClear() {
        memory = 0.0
    }

    fun memoryRecall(): Double = memory

    fun memoryStore(value: Double) {
        memory = value
    }

    fun memoryAdd(value: Double) {
        memory += value
    }

    fun memorySubtract(value: Double) {
        memory -= value
    }

    fun saveState(): String = Json.encodeToString(CalculatorState(
        angleMode = angleMode,
        inverseMode = inverseMode,
        memory = memory,
        history = history.toList()
    ))

    fun restoreState(json: String) {
        val state = Json.decodeFromString<CalculatorState>(json)
        angleMode = state.angleMode
        inverseMode = state.inverseMode
        memory = state.memory
        history.clear()
        history.addAll(state.history)
    }

    sealed interface EngineResult {
        data class Success(val value: Double) : EngineResult
        data class Error(val code: EngineError, val message: String?) : EngineResult
    }

    enum class EngineError {
        SYNTAX_ERROR,
        MATH_ERROR,
        OVERFLOW,
        INVALID_INPUT
    }
}