package com.dev.tracker_presentation.tracker_overview.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev.core.R
import com.dev.core_ui.ui.theme.CarbColor
import com.dev.core_ui.ui.theme.FatColor
import com.dev.core_ui.ui.theme.ProteinColor
import com.dev.tracker_presentation.component.UnitDisplay

@Composable
fun NutrientBar(
    modifier: Modifier = Modifier,
    carbs: Int,
    protein: Int,
    fat: Int,
    calories: Int,
    caloriesGoal: Int
) {
    val background = MaterialTheme.colors.background
    val caloriesExceedColor = MaterialTheme.colors.error
    val carbWidthRatio = remember {
        Animatable(0f)
    }
    val proteinWidthRatio = remember {
        Animatable(0f)
    }
    val fatWidthRatio = remember {
        Animatable(0f)
    }

    LaunchedEffect(carbs) {
        carbWidthRatio.animateTo(
            targetValue = (carbs * 4f) / caloriesGoal
        )
    }
    LaunchedEffect(protein) {
        proteinWidthRatio.animateTo(
            targetValue = (protein * 4f) / caloriesGoal
        )
    }
    LaunchedEffect(fat) {
        fatWidthRatio.animateTo(
            targetValue = (fat * 9f) / caloriesGoal
        )
    }

    Canvas(
        modifier = modifier
    ) {
        if(calories <= caloriesGoal) {
            val carbsWidth = carbWidthRatio.value * size.width
            val proteinWidth = proteinWidthRatio.value * size.width
            val fatWidth = fatWidthRatio.value * size.width

            drawRoundRect(
                color = background,
                size = size,
                cornerRadius = CornerRadius(100f)
            )

            drawRoundRect(
                color = FatColor,
                size = Size(
                    width = carbsWidth + proteinWidth + fatWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            drawRoundRect(
                color = ProteinColor,
                size = Size(
                    width = carbsWidth + proteinWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )

            drawRoundRect(
                color = CarbColor,
                size = Size(
                    width = carbsWidth,
                    height = size.height
                ),
                cornerRadius = CornerRadius(100f)
            )
        } else {
            drawRoundRect(
                color = caloriesExceedColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )
        }
    }
}

@Composable
fun NutrientBarInfo(
    modifier: Modifier = Modifier,
    value: Int,
    goal: Int,
    name: String,
    color: Color,
    strokeWidth: Dp = 8.dp
) {
    val background = MaterialTheme.colors.background
    val goalExceedColor = MaterialTheme.colors.error
    val angleRatio = remember {
        Animatable(0f)
    }

    LaunchedEffect(value) {
        angleRatio.animateTo(
            targetValue = if(goal>0) value/goal.toFloat() else 0f,
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            drawArc(
                color = if(value <= goal) background else goalExceedColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )

            if(value <= goal) {
                drawArc(
                    color = color,
                    startAngle = 90f,
                    sweepAngle = 360f * angleRatio.value,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UnitDisplay(
                modifier = Modifier,
                amount = value,
                unit = stringResource(id = R.string.grams),
                amountColor = if(value <= goal) MaterialTheme.colors.onPrimary else goalExceedColor,
                unitColor = if(value <= goal) MaterialTheme.colors.onPrimary else goalExceedColor
            )

            Text(
                text = name,
                style = MaterialTheme.typography.body1.copy(
                    color = if(value <= goal) MaterialTheme.colors.onPrimary else goalExceedColor,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }
}