package com.example.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CalculationHistory
import com.example.data.HistoryRepository
import com.example.data.ThemeMode
import com.example.data.UserPreferences
import com.example.domain.CalculationResult
import com.example.domain.CalculatorEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorUiState(
    val expression: String = "",
    val livePreview: String? = null,
    val lastCalculation: String? = null,
    val errorMessage: String? = null,
    val isEvaluated: Boolean = false,
    val isHistorySheetOpen: Boolean = false,
    val isSettingsDialogOpen: Boolean = false
)

class CalculatorViewModel(
    private val repository: HistoryRepository,
    private val preferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    val historyList: StateFlow<List<CalculationHistory>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val themeMode: StateFlow<ThemeMode> = preferences.themeMode
    val hapticEnabled: StateFlow<Boolean> = preferences.hapticEnabled

    fun onDigit(digit: String) {
        _uiState.update { current ->
            if (current.errorMessage != null || current.isEvaluated) {
                // Start fresh if user presses digit after evaluation or error
                val newExpr = if (digit == "00") "0" else digit
                current.copy(
                    expression = newExpr,
                    errorMessage = null,
                    isEvaluated = false,
                    livePreview = null
                )
            } else {
                val newExpr = if (current.expression == "0") {
                    if (digit == "00") "0" else digit
                } else {
                    current.expression + digit
                }
                current.copy(
                    expression = newExpr,
                    livePreview = CalculatorEngine.evaluateLivePreview(newExpr)
                )
            }
        }
    }

    fun onOperator(operator: String) {
        _uiState.update { current ->
            val expr = current.expression

            if (current.errorMessage != null) {
                return@update current.copy(errorMessage = null)
            }

            if (expr.isEmpty()) {
                if (operator == "-") {
                    return@update current.copy(expression = "-", isEvaluated = false)
                }
                return@update current.copy(expression = "0 $operator ", isEvaluated = false)
            }

            val trimmed = expr.trimEnd()
            val lastChar = trimmed.lastOrNull()

            // If last char is an operator, replace it
            val isLastOperator = lastChar == '+' || lastChar == '-' || lastChar == '×' || lastChar == '÷'
            val newExpr = if (isLastOperator) {
                trimmed.dropLast(1).trimEnd() + " $operator "
            } else {
                "$trimmed $operator "
            }

            current.copy(
                expression = newExpr,
                isEvaluated = false,
                livePreview = CalculatorEngine.evaluateLivePreview(newExpr)
            )
        }
    }

    fun onDecimal() {
        _uiState.update { current ->
            if (current.errorMessage != null || current.isEvaluated) {
                return@update current.copy(
                    expression = "0.",
                    errorMessage = null,
                    isEvaluated = false,
                    livePreview = null
                )
            }

            val expr = current.expression
            if (expr.isEmpty()) {
                return@update current.copy(expression = "0.")
            }

            // Find current number token
            val tokens = expr.split(" ", "+", "-", "×", "÷")
            val currentToken = tokens.lastOrNull() ?: ""

            if (currentToken.contains('.')) {
                return@update current // already has decimal point
            }

            val newExpr = if (currentToken.isEmpty() || expr.endsWith(" ")) {
                expr + "0."
            } else {
                "$expr."
            }

            current.copy(
                expression = newExpr,
                livePreview = CalculatorEngine.evaluateLivePreview(newExpr)
            )
        }
    }

    fun onPercent() {
        _uiState.update { current ->
            val expr = current.expression.trimEnd()
            if (expr.isEmpty() || current.errorMessage != null) return@update current

            val lastChar = expr.last()
            if (!lastChar.isDigit()) return@update current

            val newExpr = "$expr%"
            current.copy(
                expression = newExpr,
                livePreview = CalculatorEngine.evaluateLivePreview(newExpr)
            )
        }
    }

    fun onNegate() {
        _uiState.update { current ->
            val expr = current.expression.trimEnd()
            if (expr.isEmpty() || current.errorMessage != null) return@update current

            // Parse last number and toggle its sign
            val lastSpace = expr.lastIndexOf(' ')
            if (lastSpace == -1) {
                // Single number
                val toggled = if (expr.startsWith('-')) expr.drop(1) else "-$expr"
                current.copy(
                    expression = toggled,
                    livePreview = CalculatorEngine.evaluateLivePreview(toggled)
                )
            } else {
                val prefix = expr.substring(0, lastSpace + 1)
                val token = expr.substring(lastSpace + 1)
                if (token.isEmpty()) return@update current

                val toggledToken = if (token.startsWith('-')) token.drop(1) else "-$token"
                val newExpr = prefix + toggledToken
                current.copy(
                    expression = newExpr,
                    livePreview = CalculatorEngine.evaluateLivePreview(newExpr)
                )
            }
        }
    }

    fun onBackspace() {
        _uiState.update { current ->
            if (current.errorMessage != null) {
                return@update current.copy(errorMessage = null)
            }
            if (current.expression.isEmpty()) return@update current

            var newExpr = current.expression
            if (newExpr.endsWith(" ")) {
                // Pop trailing space and operator
                newExpr = newExpr.trimEnd()
                if (newExpr.isNotEmpty()) {
                    newExpr = newExpr.dropLast(1).trimEnd()
                }
            } else {
                newExpr = newExpr.dropLast(1)
            }

            current.copy(
                expression = newExpr,
                livePreview = CalculatorEngine.evaluateLivePreview(newExpr),
                isEvaluated = false
            )
        }
    }

    fun onClear() {
        _uiState.update {
            it.copy(
                expression = "",
                livePreview = null,
                errorMessage = null,
                isEvaluated = false
            )
        }
    }

    fun onEquals() {
        val current = _uiState.value
        if (current.expression.isBlank() || current.errorMessage != null) return

        when (val result = CalculatorEngine.evaluate(current.expression)) {
            is CalculationResult.Success -> {
                val formattedResult = result.formatted
                val originalExpr = current.expression.trim()

                // Save to Room DB asynchronously
                viewModelScope.launch {
                    repository.insert(originalExpr, formattedResult)
                }

                _uiState.update {
                    it.copy(
                        expression = formattedResult,
                        livePreview = null,
                        lastCalculation = "$originalExpr = $formattedResult",
                        errorMessage = null,
                        isEvaluated = true
                    )
                }
            }
            is CalculationResult.DivideByZero -> {
                _uiState.update {
                    it.copy(
                        errorMessage = "تقسیم بر صفر امکان‌پذیر نیست",
                        livePreview = null
                    )
                }
            }
            is CalculationResult.Error -> {
                _uiState.update {
                    it.copy(
                        errorMessage = "خطا در عبارت ریاضی",
                        livePreview = null
                    )
                }
            }
        }
    }

    fun onSelectHistory(item: CalculationHistory) {
        _uiState.update {
            it.copy(
                expression = item.result,
                livePreview = null,
                lastCalculation = "${item.expression} = ${item.result}",
                errorMessage = null,
                isEvaluated = true
            )
        }
    }

    fun onDeleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun onClearAllHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        preferences.setThemeMode(mode)
    }

    fun setHapticEnabled(enabled: Boolean) {
        preferences.setHapticEnabled(enabled)
    }

    fun openHistorySheet() {
        _uiState.update { it.copy(isHistorySheetOpen = true) }
    }

    fun closeHistorySheet() {
        _uiState.update { it.copy(isHistorySheetOpen = false) }
    }

    fun openSettingsDialog() {
        _uiState.update { it.copy(isSettingsDialogOpen = true) }
    }

    fun closeSettingsDialog() {
        _uiState.update { it.copy(isSettingsDialogOpen = false) }
    }
}

class CalculatorViewModelFactory(
    private val repository: HistoryRepository,
    private val preferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
