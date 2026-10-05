package com.example

import com.example.domain.CalculationResult
import com.example.domain.CalculatorEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class CalculatorEngineTest {

    @Test
    fun testBasicAddition() {
        val result = CalculatorEngine.evaluate("15 + 25")
        assertTrue(result is CalculationResult.Success)
        assertEquals("40", (result as CalculationResult.Success).formatted)
    }

    @Test
    fun testOperatorPrecedence() {
        val result = CalculatorEngine.evaluate("2 + 3 × 4")
        assertTrue(result is CalculationResult.Success)
        assertEquals("14", (result as CalculationResult.Success).formatted)
    }

    @Test
    fun testDivisionByZero() {
        val result = CalculatorEngine.evaluate("50 ÷ 0")
        assertTrue(result is CalculationResult.DivideByZero)
    }

    @Test
    fun testPercentageCalculation() {
        val result = CalculatorEngine.evaluate("200 × 15%")
        assertTrue(result is CalculationResult.Success)
        assertEquals("30", (result as CalculationResult.Success).formatted)
    }

    @Test
    fun testDecimalPrecision() {
        val result = CalculatorEngine.evaluate("0.1 + 0.2")
        assertTrue(result is CalculationResult.Success)
        assertEquals("0.3", (result as CalculationResult.Success).formatted)
    }

    @Test
    fun testNegativeNumbers() {
        val result = CalculatorEngine.evaluate("-10 + 25")
        assertTrue(result is CalculationResult.Success)
        assertEquals("15", (result as CalculationResult.Success).formatted)
    }

    @Test
    fun testLivePreview() {
        val preview = CalculatorEngine.evaluateLivePreview("12 × 5 + ")
        assertEquals("60", preview)
    }
}
