package com.example.ui.calculator.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

enum class ButtonType {
    NUMBER,
    OPERATOR,
    FUNCTION,
    EQUALS
}

@Composable
fun CalculatorButton(
    text: String? = null,
    icon: ImageVector? = null,
    buttonType: ButtonType = ButtonType.NUMBER,
    backgroundColor: Color,
    contentColor: Color,
    contentDescriptionText: String,
    testTag: String,
    hapticEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f),
        label = "btnScale"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .sizeIn(minWidth = 56.dp, minHeight = 56.dp)
            .padding(4.dp)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 1.dp else if (buttonType == ButtonType.EQUALS) 6.dp else 2.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = contentColor.copy(alpha = 0.3f)),
                role = Role.Button,
                onClick = {
                    if (hapticEnabled) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    onClick()
                }
            )
            .testTag(testTag)
            .semantics {
                role = Role.Button
                contentDescription = contentDescriptionText
            },
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.fillMaxSize(0.42f)
            )
        } else if (text != null) {
            val fontSize = when {
                text.length > 2 -> 20.sp
                buttonType == ButtonType.OPERATOR -> 30.sp
                buttonType == ButtonType.EQUALS -> 32.sp
                else -> 26.sp
            }
            Text(
                text = text,
                color = contentColor,
                fontSize = fontSize,
                fontWeight = if (buttonType == ButtonType.NUMBER) FontWeight.Medium else FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
