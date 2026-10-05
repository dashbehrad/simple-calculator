package com.example.domain

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

sealed class CalculationResult {
    data class Success(val value: BigDecimal, val formatted: String) : CalculationResult()
    data object DivideByZero : CalculationResult()
    data class Error(val message: String) : CalculationResult()
}

object CalculatorEngine {
    private val mathContext = MathContext(16, RoundingMode.HALF_UP)

    private val decimalSymbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    private val numberFormatter = DecimalFormat("#,##0.##########", decimalSymbols).apply {
        maximumFractionDigits = 10
        isGroupingUsed = true
    }

    private val scientificFormatter = DecimalFormat("0.######E0", decimalSymbols)

    /**
     * Formats a raw BigDecimal into a clean user-facing string.
     */
    fun formatNumber(number: BigDecimal): String {
        val absVal = number.abs()
        return if (absVal != BigDecimal.ZERO && (absVal >= BigDecimal("1000000000000") || absVal < BigDecimal("0.000001"))) {
            scientificFormatter.format(number)
        } else {
            // Strip trailing zeroes from scale
            val stripped = number.stripTrailingZeros()
            numberFormatter.format(stripped)
        }
    }

    /**
     * Evaluates a mathematical expression string containing numbers and operators:
     * +, -, × (or *), ÷ (or /), and percentages (%).
     */
    fun evaluate(expression: String): CalculationResult {
        val sanitized = expression
            .replace("×", "*")
            .replace("÷", "/")
            .replace(",", "")
            .trim()

        if (sanitized.isEmpty()) {
            return CalculationResult.Success(BigDecimal.ZERO, "0")
        }

        return try {
            val tokens = tokenize(sanitized)
            if (tokens.isEmpty()) {
                return CalculationResult.Success(BigDecimal.ZERO, "0")
            }
            val result = evaluateTokens(tokens)
            CalculationResult.Success(result, formatNumber(result))
        } catch (e: ArithmeticException) {
            CalculationResult.DivideByZero
        } catch (e: Exception) {
            CalculationResult.Error(e.message ?: "Invalid expression")
        }
    }

    /**
     * Computes live preview if the expression is partial, e.g. "12 + 8" -> 20.
     * If the expression ends with an operator like "12 + ", evaluates "12".
     */
    fun evaluateLivePreview(expression: String): String? {
        var clean = expression.trim()
        if (clean.isEmpty()) return null

        // Strip trailing operators
        while (clean.isNotEmpty() && isOperator(clean.last())) {
            clean = clean.dropLast(1).trim()
        }

        if (clean.isEmpty()) return null

        // Only show preview if there is an operator involved
        val hasOperator = clean.any { it == '+' || it == '-' || it == '×' || it == '÷' || it == '*' || it == '/' || it == '%' }
        if (!hasOperator) return null

        return when (val res = evaluate(clean)) {
            is CalculationResult.Success -> res.formatted
            else -> null
        }
    }

    private fun isOperator(c: Char): Boolean = c == '+' || c == '-' || c == '×' || c == '÷' || c == '*' || c == '/'

    private sealed class Token {
        data class Number(val value: BigDecimal) : Token()
        data class Operator(val op: Char) : Token()
    }

    private fun tokenize(expr: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        val length = expr.length

        while (i < length) {
            val c = expr[i]
            when {
                c.isWhitespace() -> {
                    i++
                }
                c == '+' || c == '*' || c == '/' -> {
                    tokens.add(Token.Operator(c))
                    i++
                }
                c == '-' -> {
                    // Check if '-' is unary (negative number) or binary subtraction
                    val isUnary = tokens.isEmpty() || tokens.last() is Token.Operator
                    if (isUnary) {
                        // Parse negative number
                        val start = i
                        i++ // Skip '-'
                        while (i < length && (expr[i].isDigit() || expr[i] == '.')) {
                            i++
                        }
                        val numStr = expr.substring(start, i)
                        if (numStr == "-") {
                            // Lone '-' followed by operator
                            tokens.add(Token.Operator('-'))
                        } else {
                            var num = BigDecimal(numStr, mathContext)
                            if (i < length && expr[i] == '%') {
                                num = num.divide(BigDecimal("100"), mathContext)
                                i++
                            }
                            tokens.add(Token.Number(num))
                        }
                    } else {
                        tokens.add(Token.Operator('-'))
                        i++
                    }
                }
                c.isDigit() || c == '.' -> {
                    val start = i
                    while (i < length && (expr[i].isDigit() || expr[i] == '.')) {
                        i++
                    }
                    var num = BigDecimal(expr.substring(start, i), mathContext)
                    if (i < length && expr[i] == '%') {
                        num = num.divide(BigDecimal("100"), mathContext)
                        i++
                    }
                    tokens.add(Token.Number(num))
                }
                c == '%' -> {
                    // Standalone percentage applied to last token
                    if (tokens.isNotEmpty() && tokens.last() is Token.Number) {
                        val lastNum = (tokens.removeAt(tokens.lastIndex) as Token.Number).value
                        tokens.add(Token.Number(lastNum.divide(BigDecimal("100"), mathContext)))
                    }
                    i++
                }
                else -> {
                    i++
                }
            }
        }
        return tokens
    }

    private fun evaluateTokens(tokens: List<Token>): BigDecimal {
        if (tokens.isEmpty()) return BigDecimal.ZERO

        // First pass: Handle * and /
        val intermediateTokens = mutableListOf<Token>()
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]
            if (token is Token.Operator && (token.op == '*' || token.op == '/')) {
                val left = if (intermediateTokens.isNotEmpty() && intermediateTokens.last() is Token.Number) {
                    (intermediateTokens.removeAt(intermediateTokens.lastIndex) as Token.Number).value
                } else {
                    BigDecimal.ZERO
                }
                val nextToken = if (i + 1 < tokens.size) tokens[i + 1] else null
                val right = if (nextToken is Token.Number) {
                    i++
                    nextToken.value
                } else {
                    BigDecimal.ZERO
                }

                val result = if (token.op == '*') {
                    left.multiply(right, mathContext)
                } else {
                    if (right.compareTo(BigDecimal.ZERO) == 0) {
                        throw ArithmeticException("Division by zero")
                    }
                    left.divide(right, mathContext)
                }
                intermediateTokens.add(Token.Number(result))
            } else {
                intermediateTokens.add(token)
            }
            i++
        }

        // Second pass: Handle + and -
        if (intermediateTokens.isEmpty()) return BigDecimal.ZERO
        var total = if (intermediateTokens[0] is Token.Number) {
            (intermediateTokens[0] as Token.Number).value
        } else {
            BigDecimal.ZERO
        }

        var j = 1
        while (j < intermediateTokens.size) {
            val opToken = intermediateTokens[j]
            if (opToken is Token.Operator && j + 1 < intermediateTokens.size) {
                val rightToken = intermediateTokens[j + 1]
                val right = if (rightToken is Token.Number) rightToken.value else BigDecimal.ZERO

                total = when (opToken.op) {
                    '+' -> total.add(right, mathContext)
                    '-' -> total.subtract(right, mathContext)
                    else -> total
                }
                j += 2
            } else {
                j++
            }
        }

        return total
    }
}
