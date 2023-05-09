package com.dev.onboarding_presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.dev.core_ui.LocalSpacing

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.button
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(64.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(LocalSpacing.current.spaceSmall),
            text = text,
            style = textStyle.copy(
                color = MaterialTheme.colors.onPrimary
            )
        )
    }
}

@Composable
fun SelectableButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    color: Color,
    selectedColor: Color,
    onClick: () -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.button
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(64.dp))
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(64.dp)
            )
            .background(
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(64.dp)
            )
            .clickable {
                onClick()
            }
            .padding(LocalSpacing.current.spaceMedium)
    ) {
        Text(
            modifier = Modifier,
            text = text,
            style = textStyle.copy(
                color = if(isSelected) selectedColor else color
            )
        )
    }
}