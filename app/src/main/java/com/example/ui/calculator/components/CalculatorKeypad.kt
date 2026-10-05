package com.example.ui.calculator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.CalculatorTheme

@Composable
fun CalculatorKeypad(
    hapticEnabled: Boolean,
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onNegateClick: () -> Unit,
    onPercentClick: () -> Unit,
    onDecimalClick: () -> Unit,
    onEqualsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalculatorTheme.colors
    val spacing = 8.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Row 1: AC, +/-, %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            CalculatorButton(
                text = "AC",
                buttonType = ButtonType.FUNCTION,
                backgroundColor = colors.functionBtnBg,
                contentColor = colors.functionBtnText,
                contentDescriptionText = stringResource(R.string.cd_clear),
                testTag = "btn_ac",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = onClearClick
            )
            CalculatorButton(
                text = "+/-",
                buttonType = ButtonType.FUNCTION,
                backgroundColor = colors.functionBtnBg,
                contentColor = colors.functionBtnText,
                contentDescriptionText = stringResource(R.string.cd_negate),
                testTag = "btn_negate",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = onNegateClick
            )
            CalculatorButton(
                text = "%",
                buttonType = ButtonType.FUNCTION,
                backgroundColor = colors.functionBtnBg,
                contentColor = colors.functionBtnText,
                contentDescriptionText = stringResource(R.string.cd_percent),
                testTag = "btn_percent",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = onPercentClick
            )
            CalculatorButton(
                text = "÷",
                buttonType = ButtonType.OPERATOR,
                backgroundColor = colors.operatorBtnBg,
                contentColor = colors.operatorBtnText,
                contentDescriptionText = stringResource(R.string.cd_divide),
                testTag = "btn_divide",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onOperatorClick("÷") }
            )
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            CalculatorButton(
                text = "7",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "7",
                testTag = "btn_7",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("7") }
            )
            CalculatorButton(
                text = "8",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "8",
                testTag = "btn_8",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("8") }
            )
            CalculatorButton(
                text = "9",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "9",
                testTag = "btn_9",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("9") }
            )
            CalculatorButton(
                text = "×",
                buttonType = ButtonType.OPERATOR,
                backgroundColor = colors.operatorBtnBg,
                contentColor = colors.operatorBtnText,
                contentDescriptionText = stringResource(R.string.cd_multiply),
                testTag = "btn_multiply",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onOperatorClick("×") }
            )
        }

        // Row 3: 4, 5, 6, -
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            CalculatorButton(
                text = "4",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "4",
                testTag = "btn_4",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("4") }
            )
            CalculatorButton(
                text = "5",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "5",
                testTag = "btn_5",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("5") }
            )
            CalculatorButton(
                text = "6",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "6",
                testTag = "btn_6",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("6") }
            )
            CalculatorButton(
                text = "-",
                buttonType = ButtonType.OPERATOR,
                backgroundColor = colors.operatorBtnBg,
                contentColor = colors.operatorBtnText,
                contentDescriptionText = stringResource(R.string.cd_subtract),
                testTag = "btn_subtract",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onOperatorClick("-") }
            )
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            CalculatorButton(
                text = "1",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "1",
                testTag = "btn_1",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("1") }
            )
            CalculatorButton(
                text = "2",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "2",
                testTag = "btn_2",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("2") }
            )
            CalculatorButton(
                text = "3",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "3",
                testTag = "btn_3",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("3") }
            )
            CalculatorButton(
                text = "+",
                buttonType = ButtonType.OPERATOR,
                backgroundColor = colors.operatorBtnBg,
                contentColor = colors.operatorBtnText,
                contentDescriptionText = stringResource(R.string.cd_add),
                testTag = "btn_add",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onOperatorClick("+") }
            )
        }

        // Row 5: 0, 00, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            CalculatorButton(
                text = "0",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "0",
                testTag = "btn_0",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("0") }
            )
            CalculatorButton(
                text = "00",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = "00",
                testTag = "btn_00",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = { onDigitClick("00") }
            )
            CalculatorButton(
                text = ".",
                buttonType = ButtonType.NUMBER,
                backgroundColor = colors.numberBtnBg,
                contentColor = colors.numberBtnText,
                contentDescriptionText = stringResource(R.string.cd_dot),
                testTag = "btn_dot",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = onDecimalClick
            )
            CalculatorButton(
                text = "=",
                buttonType = ButtonType.EQUALS,
                backgroundColor = colors.equalsBtnBg,
                contentColor = colors.equalsBtnText,
                contentDescriptionText = stringResource(R.string.cd_equals),
                testTag = "btn_equals",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f),
                onClick = onEqualsClick
            )
        }
    }
}
