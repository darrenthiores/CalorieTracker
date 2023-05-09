package com.dev.tracker_presentation.tracker_overview.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.core_ui.LocalSpacing
import com.dev.core.R
import com.dev.core_ui.ui.theme.CalorieTrackerTheme

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.primary
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(64.dp))
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(64.dp)
            )
            .padding(spacing.spaceMedium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add),
            tint = color
        )
        
        Spacer(modifier = Modifier.width(spacing.spaceMedium))

        Text(
            modifier = Modifier,
            text = text,
            style = MaterialTheme.typography.button.copy(
                color = color
            )
        )
    }
}

@Preview
@Composable
private fun AddButtonPreview() {
    CalorieTrackerTheme {
        AddButton(
            text = "Add",
            onClick = {  }
        )
    }
}