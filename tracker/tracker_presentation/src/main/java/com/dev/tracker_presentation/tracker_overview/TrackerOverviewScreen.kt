package com.dev.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.core.R
import com.dev.core_ui.LocalSpacing
import com.dev.tracker_presentation.tracker_overview.component.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TrackerOverviewScreen(
    viewModel: TrackerOverviewViewModel = hiltViewModel(),
    onNavigateToSearch: (String, Int, Int, Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        item {
            NutrientHeader(
                state = state
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            DaySelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium),
                date = state.date,
                onPreviousDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick)
                }
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }

        items(
            items = state.meals,
            key = { meal -> meal.mealType.name }
        ) { meal ->
            ExpandableMeal(
                modifier = Modifier
                    .fillMaxWidth(),
                meal = meal,
                onToggleClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceSmall)
                    ) {
                        state.trackedFood
                            .filter { it.mealType == meal.mealType }
                            .forEach { food ->
                                TrackerFoodItem(
                                    modifier = Modifier,
                                    trackedFood = food,
                                    onDeleteClick = {
                                        viewModel.onEvent(
                                            TrackerOverviewEvent
                                                .OnDeleteTrackedFoodClick(food)
                                        )
                                    }
                                )

                                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                            }

                        AddButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context)
                            ),
                            onClick = {
                                onNavigateToSearch(
                                    meal.mealType.name,
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year
                                )
                            }
                        )
                    }
                }
            )
        }
    }
}