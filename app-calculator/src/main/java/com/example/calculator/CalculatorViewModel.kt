package com.example.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.calculator.engine.Evaluator
import com.example.calculator.engine.Tokenizer
import com.example.calculator.model.CalculatorDatabase
import com.example.calculator.model.HistoryEntry
import com.example.calculator.utils.NumberFormatter
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = CalculatorDatabase.getInstance(application).historyDao()

    private val _expressionText = MutableLiveData("")
    val expressionText: LiveData<String> = _expressionText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    private val _errorText = MutableLiveData<String?>(null)
    val errorText: LiveData<String?> = _errorText

    val history: LiveData<List<HistoryEntry>> = dao.getAll().asLiveData()

    private var currentInput = ""

    private val displayOperators = mapOf(
        "sin" to "sin(", "cos" to "cos(", "tan" to "tan(",
        "asin" to "asin(", "acos" to "acos(", "atan" to "atan(",
        "log" to "log(", "ln" to "ln(", "sqrt" to "√(", "cbrt" to "∛(",
        "square" to "²", "cube" to "³"
    )

    fun onDigit(digit: String) {
        _errorText.value = null
        currentInput += digit
        updateDisplay()
        evaluateCurrent()
    }

    fun onDecimal() {
        _errorText.value = null
        val lastNum = currentInput.split(Regex("[+\\-×÷^()]")).lastOrNull() ?: ""
        if ("." !in lastNum) {
            currentInput += if (lastNum.isEmpty()) "0." else "."
            updateDisplay()
        }
    }

    fun onOperator(op: String) {
        _errorText.value = null
        if (currentInput.isEmpty() && op == "-") {
            currentInput = "-"
            updateDisplay()
            return
        }
        if (currentInput.isEmpty()) return
        val lastChar = currentInput.lastOrNull()
        if (lastChar in listOf('+', '-', '×', '÷', '^')) {
            currentInput = currentInput.dropLast(1)
        }
        currentInput += op
        updateDisplay()
    }

    fun onFunction(name: String) {
        _errorText.value = null
        if (currentInput.isNotEmpty() && currentInput.last().isDigit()) {
            currentInput += "×"
        }
        currentInput += displayOperators[name] ?: "${name}("
        updateDisplay()
    }

    fun onParentheses() {
        _errorText.value = null
        val openCount = currentInput.count { it == '(' }
        val closeCount = currentInput.count { it == ')' }
        currentInput += if (openCount > closeCount) ")" else "("
        updateDisplay()
    }

    fun onPercent() {
        _errorText.value = null
        currentInput += "%"
        updateDisplay()
        evaluateCurrent()
    }

    fun onFactorial() {
        _errorText.value = null
        currentInput += "!"
        updateDisplay()
        evaluateCurrent()
    }

    fun onPower() {
        onOperator("^")
    }

    fun onConstant(name: String) {
        _errorText.value = null
        if (currentInput.isNotEmpty() && currentInput.last().isDigit()) {
            currentInput += "×"
        }
        currentInput += name
        updateDisplay()
        evaluateCurrent()
    }

    fun onClear() {
        currentInput = ""
        _expressionText.value = ""
        _resultText.value = "0"
        _errorText.value = null
    }

    fun onBackspace() {
        _errorText.value = null
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            updateDisplay()
            if (currentInput.isNotEmpty()) evaluateCurrent() else _resultText.value = "0"
        }
    }

    fun onEquals() {
        if (currentInput.isEmpty()) return
        try {
            val displayExpr = currentInput
            val evalExpr = normalizeExpression(currentInput)

            val tokens = Tokenizer(evalExpr).tokenize()
            val result = Evaluator.evaluate(tokens)

            if (result.error != null) {
                _errorText.value = result.error
                return
            }

            val formatted = NumberFormatter.format(result.value!!)
            _resultText.value = formatted
            _expressionText.value = displayExpr

            viewModelScope.launch {
                dao.insert(HistoryEntry(expression = displayExpr, result = formatted))
            }
            currentInput = formatted
        } catch (e: Exception) {
            _errorText.value = "表达式错误"
        }
    }

    fun toggleAngleMode() {
        Evaluator.isDegreeMode = !Evaluator.isDegreeMode
    }

    fun isDegreeMode() = Evaluator.isDegreeMode

    fun loadFromHistory(entry: HistoryEntry) {
        currentInput = entry.result
        _expressionText.value = entry.expression
        _resultText.value = entry.result
    }

    fun clearHistory() {
        viewModelScope.launch { dao.deleteAll() }
    }

    private fun updateDisplay() {
        _expressionText.value = currentInput
    }

    private fun evaluateCurrent() {
        if (currentInput.isEmpty()) return
        try {
            val evalExpr = normalizeExpression(currentInput)
            val tokens = Tokenizer(evalExpr).tokenize()
            val result = Evaluator.evaluate(tokens)
            if (result.error != null) {
                _errorText.value = result.error
                return
            }
            _resultText.value = NumberFormatter.format(result.value!!)
            _errorText.value = null
        } catch (e: Exception) {
            _resultText.value = "?"
        }
    }

    private fun normalizeExpression(input: String): String {
        return input
            .replace("×", "*")
            .replace("÷", "/")
            .replace("√(", "sqrt(")
            .replace("∛(", "cbrt(")
            .replace("²", "^2")
            .replace("³", "^3")
    }
}
