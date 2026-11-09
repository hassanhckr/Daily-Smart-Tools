package com.example.ui.screens.tools

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat

data class CalculationHistoryItem(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CalculatorUiState(
    val currentInput: String = "0",
    val expressionText: String = "",
    val error: String? = null,
    val history: List<CalculationHistoryItem> = emptyList()
)

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private var previousValue: Double? = null
    private var pendingOperation: String? = null
    private var isNewInput: Boolean = true
    private val decimalFormat = DecimalFormat("#,##0.########")

    fun onDigit(digit: String) {
        val current = _uiState.value.currentInput
        val updated = if (isNewInput || current == "0") {
            digit
        } else {
            current + digit
        }
        isNewInput = false
        _uiState.value = _uiState.value.copy(
            currentInput = updated,
            error = null
        )
    }

    fun onDecimal() {
        val current = _uiState.value.currentInput
        if (isNewInput) {
            _uiState.value = _uiState.value.copy(currentInput = "0.", error = null)
            isNewInput = false
        } else if (!current.contains(".")) {
            _uiState.value = _uiState.value.copy(currentInput = "$current.", error = null)
        }
    }

    fun onToggleSign() {
        val current = _uiState.value.currentInput
        if (current == "0") return
        val updated = if (current.startsWith("-")) {
            current.substring(1)
        } else {
            "-$current"
        }
        _uiState.value = _uiState.value.copy(currentInput = updated, error = null)
    }

    fun onPercentage() {
        try {
            val value = _uiState.value.currentInput.replace(",", "").toDouble()
            val result = value / 100.0
            val formatted = formatNumber(result)
            _uiState.value = _uiState.value.copy(
                currentInput = formatted,
                error = null
            )
            isNewInput = true
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(error = "Invalid format")
        }
    }

    fun onOperation(operation: String) {
        val currentStr = _uiState.value.currentInput.replace(",", "")
        val currentVal = currentStr.toDoubleOrNull() ?: 0.0

        if (previousValue != null && pendingOperation != null && !isNewInput) {
            calculateIntermediate()
        } else {
            previousValue = currentVal
        }

        pendingOperation = operation
        isNewInput = true

        val prevFormatted = formatNumber(previousValue ?: 0.0)
        val opSymbol = when (operation) {
            "+" -> "+"
            "-" -> "−"
            "*" -> "×"
            "/" -> "÷"
            else -> operation
        }
        _uiState.value = _uiState.value.copy(
            expressionText = "$prevFormatted $opSymbol",
            error = null
        )
    }

    private fun calculateIntermediate() {
        val prev = previousValue ?: return
        val current = _uiState.value.currentInput.replace(",", "").toDoubleOrNull() ?: return
        val op = pendingOperation ?: return

        val result: Double? = when (op) {
            "+" -> prev + current
            "-" -> prev - current
            "*" -> prev * current
            "/" -> if (current == 0.0) null else prev / current
            else -> current
        }

        if (result == null) {
            _uiState.value = _uiState.value.copy(error = "Cannot divide by zero")
            previousValue = null
            pendingOperation = null
            isNewInput = true
        } else {
            previousValue = result
            _uiState.value = _uiState.value.copy(
                currentInput = formatNumber(result),
                error = null
            )
        }
    }

    fun onEquals() {
        val prev = previousValue ?: return
        val currentStr = _uiState.value.currentInput.replace(",", "")
        val current = currentStr.toDoubleOrNull() ?: return
        val op = pendingOperation ?: return

        val opSymbol = when (op) {
            "+" -> "+"
            "-" -> "−"
            "*" -> "×"
            "/" -> "÷"
            else -> op
        }

        if (op == "/" && current == 0.0) {
            _uiState.value = _uiState.value.copy(
                error = "Cannot divide by zero",
                currentInput = "Error",
                expressionText = "$prev $opSymbol 0"
            )
            previousValue = null
            pendingOperation = null
            isNewInput = true
            return
        }

        val result = when (op) {
            "+" -> prev + current
            "-" -> prev - current
            "*" -> prev * current
            "/" -> prev / current
            else -> current
        }

        val formattedResult = formatNumber(result)
        val fullExpr = "${formatNumber(prev)} $opSymbol ${formatNumber(current)}"

        val historyItem = CalculationHistoryItem(
            expression = fullExpr,
            result = formattedResult
        )

        val newHistory = listOf(historyItem) + _uiState.value.history.take(19)

        _uiState.value = _uiState.value.copy(
            currentInput = formattedResult,
            expressionText = "$fullExpr =",
            error = null,
            history = newHistory
        )

        previousValue = null
        pendingOperation = null
        isNewInput = true
    }

    fun onClear() {
        previousValue = null
        pendingOperation = null
        isNewInput = true
        _uiState.value = _uiState.value.copy(
            currentInput = "0",
            expressionText = "",
            error = null
        )
    }

    fun onDelete() {
        if (isNewInput) return
        val current = _uiState.value.currentInput
        val updated = if (current.length <= 1 || (current.length == 2 && current.startsWith("-"))) {
            "0"
        } else {
            current.dropLast(1)
        }
        _uiState.value = _uiState.value.copy(currentInput = updated, error = null)
    }

    fun clearHistory() {
        _uiState.value = _uiState.value.copy(history = emptyList())
    }

    private fun formatNumber(number: Double): String {
        return if (number == number.toLong().toDouble()) {
            number.toLong().toString()
        } else {
            decimalFormat.format(number)
        }
    }
}
