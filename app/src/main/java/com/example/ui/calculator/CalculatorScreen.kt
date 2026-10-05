package com.example.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.ThemeMode
import com.example.ui.calculator.components.CalculatorDisplay
import com.example.ui.calculator.components.CalculatorKeypad
import com.example.ui.calculator.components.HistoryBottomSheet
import com.example.ui.calculator.components.SettingsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val hapticEnabled by viewModel.hapticEnabled.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 520.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.calculator_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick theme toggle icon (cycles: System -> Light -> Dark -> System)
                        IconButton(
                            onClick = {
                                val nextTheme = when (themeMode) {
                                    ThemeMode.SYSTEM -> ThemeMode.LIGHT
                                    ThemeMode.LIGHT -> ThemeMode.DARK
                                    ThemeMode.DARK -> ThemeMode.SYSTEM
                                }
                                viewModel.setThemeMode(nextTheme)
                            },
                            modifier = Modifier.testTag("theme_toggle_button")
                        ) {
                            val icon = when (themeMode) {
                                ThemeMode.SYSTEM -> Icons.Outlined.BrightnessAuto
                                ThemeMode.LIGHT -> Icons.Outlined.LightMode
                                ThemeMode.DARK -> Icons.Outlined.DarkMode
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(R.string.theme_settings),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // History button with count badge
                        IconButton(
                            onClick = { viewModel.openHistorySheet() },
                            modifier = Modifier.testTag("history_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (historyList.isNotEmpty()) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Text(
                                                text = if (historyList.size > 99) "99+" else historyList.size.toString(),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.History,
                                    contentDescription = stringResource(R.string.history_title),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        // Settings button
                        IconButton(
                            onClick = { viewModel.openSettingsDialog() },
                            modifier = Modifier.testTag("settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = stringResource(R.string.settings_title),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                // Calculator Display
                CalculatorDisplay(
                    expression = uiState.expression,
                    livePreview = uiState.livePreview,
                    lastCalculation = uiState.lastCalculation,
                    errorMessage = uiState.errorMessage,
                    onBackspace = { viewModel.onBackspace() },
                    onClear = { viewModel.onClear() },
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Circular Buttons Keypad
                CalculatorKeypad(
                    hapticEnabled = hapticEnabled,
                    onDigitClick = { digit -> viewModel.onDigit(digit) },
                    onOperatorClick = { op -> viewModel.onOperator(op) },
                    onClearClick = { viewModel.onClear() },
                    onNegateClick = { viewModel.onNegate() },
                    onPercentClick = { viewModel.onPercent() },
                    onDecimalClick = { viewModel.onDecimal() },
                    onEqualsClick = { viewModel.onEquals() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    // History Bottom Sheet
    if (uiState.isHistorySheetOpen) {
        HistoryBottomSheet(
            sheetState = sheetState,
            historyList = historyList,
            onDismiss = { viewModel.closeHistorySheet() },
            onSelectHistory = { item -> viewModel.onSelectHistory(item) },
            onDeleteItem = { id -> viewModel.onDeleteHistoryItem(id) },
            onClearAll = { viewModel.onClearAllHistory() }
        )
    }

    // Settings Dialog
    if (uiState.isSettingsDialogOpen) {
        SettingsDialog(
            currentThemeMode = themeMode,
            hapticEnabled = hapticEnabled,
            onThemeModeChanged = { mode -> viewModel.setThemeMode(mode) },
            onHapticChanged = { enabled -> viewModel.setHapticEnabled(enabled) },
            onDismiss = { viewModel.closeSettingsDialog() }
        )
    }
}
