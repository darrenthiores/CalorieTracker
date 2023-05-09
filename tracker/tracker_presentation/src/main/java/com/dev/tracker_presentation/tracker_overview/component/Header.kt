package com.dev.tracker_presentation.tracker_overview.component

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.core_ui.LocalSpacing
import com.dev.tracker_presentation.component.UnitDisplay
import com.dev.tracker_presentation.tracker_overview.TrackerOverviewState
import com.dev.core.R
import com.dev.core_ui.ui.theme.CarbColor
import com.dev.core_ui.ui.theme.FatColor
import com.dev.core_ui.ui.theme.ProteinColor

@Composable
fun NutrientHeader(
    modifier: Modifier = Modifier,
    state: TrackerOverviewState
) {
    val spacing = LocalSpacing.current
    val animatedCalorieCount = animateIntAsState(
        targetValue = state.totalCalories
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 54.dp,
                    bottomEnd = 54.dp
                )
            )
            .background(MaterialTheme.colors.primary)
            .padding(
                horizontal = spacing.spaceLarge,
                vertical = spacing.spaceExtraLarge
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UnitDisplay(
                modifier = Modifier
                    .align(Alignment.Bottom),
                amount = animatedCalorieCount.value,
                unit = stringResource(id = R.string.kcal),
                amountColor = MaterialTheme.colors.onPrimary,
                amountTextSize = 40.sp,
                unitColor = MaterialTheme.colors.onPrimary
            )

            Column {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.your_goal),
                    style = MaterialTheme.typography.body2.copy(
                        color = MaterialTheme.colors.onPrimary
                    )
                )

                UnitDisplay(
                    modifier = Modifier,
                    amount = state.caloriesGoal,
                    unit = stringResource(id = R.string.kcal),
                    amountColor = MaterialTheme.colors.onPrimary,
                    amountTextSize = 40.sp,
                    unitColor = MaterialTheme.colors.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.spaceSmall))

        NutrientBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            carbs = state.totalCarbs,
            protein = state.totalProtein,
            fat = state.totalFat,
            calories = state.totalCalories,
            caloriesGoal = state.caloriesGoal
        )

        Spacer(modifier = Modifier.height(spacing.spaceLarge))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientBarInfo(
                modifier = Modifier
                    .size(90.dp),
                value = state.totalCarbs,
                goal = state.carbsGoal,
                name = stringResource(id = R.string.carbs),
                color = CarbColor
            )

            NutrientBarInfo(
                modifier = Modifier
                    .size(90.dp),
                value = state.totalProtein,
                goal = state.proteinGoal,
                name = stringResource(id = R.string.protein),
                color = ProteinColor
            )

            NutrientBarInfo(
                modifier = Modifier
                    .size(90.dp),
                value = state.totalFat,
                goal = state.fatGoal,
                name = stringResource(id = R.string.fat),
                color = FatColor
            )
        }
    }
}