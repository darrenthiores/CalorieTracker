package com.dev.onboarding_presentation.activity

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.core.util.UiEvent
import com.dev.core_ui.LocalSpacing
import com.dev.core.R
import com.dev.core.domain.model.ActivityLevel
import com.dev.onboarding_presentation.component.ActionButton
import com.dev.onboarding_presentation.component.SelectableButton

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val spacing = LocalSpacing.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Success -> onNextClick()
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_activity_level),
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Row {
                SelectableButton(
                    modifier = Modifier,
                    text = stringResource(id = R.string.low),
                    isSelected = viewModel.selectedActivity is ActivityLevel.Low,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    onClick = {
                        viewModel.onActivityClick(ActivityLevel.Low)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    modifier = Modifier,
                    text = stringResource(id = R.string.medium),
                    isSelected = viewModel.selectedActivity is ActivityLevel.Medium,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    onClick = {
                        viewModel.onActivityClick(ActivityLevel.Medium)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    modifier = Modifier,
                    text = stringResource(id = R.string.high),
                    isSelected = viewModel.selectedActivity is ActivityLevel.High,
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    onClick = {
                        viewModel.onActivityClick(ActivityLevel.High)
                    },
                    textStyle = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        ActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick
        )
    }
}