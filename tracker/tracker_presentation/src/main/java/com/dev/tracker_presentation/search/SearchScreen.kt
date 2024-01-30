package com.dev.tracker_presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.core.util.UiEvent
import com.dev.core_ui.LocalSpacing
import com.dev.core.R
import com.dev.tracker_domain.model.MealType
import com.dev.tracker_presentation.search.component.SearchTextField
import com.dev.tracker_presentation.search.component.TrackableFoodItem
import java.time.LocalDate

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    mealName: String,
    date: LocalDate,
    onNavigateUp: () -> Unit,
    showSnackBar: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    showSnackBar(event.message.asString(context))
                    keyboardController?.hide()
                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(
                id = R.string.add_meal,
                mealName
            ),
            style = MaterialTheme.typography.h2
        )
        
        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        SearchTextField(
            text = state.query,
            onValueChange = {
                viewModel.onEvent(SearchEvent.OnQueryChange(it))
            },
            shouldShowHint = state.isHintVisible,
            onSearch = {
                keyboardController?.hide()
                viewModel.onEvent(SearchEvent.OnSearch)
            },
            onFocusChanged = {
                viewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
            }
        )

        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = state.trackableFood,
                key = { food -> food.food.name }
            ) { food ->
                TrackableFoodItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    trackableFoodUiState = food,
                    onClick = {
                        viewModel.onEvent(
                            SearchEvent.OnToggleTrackableFood(
                                food = food.food
                            )
                        )
                    },
                    onAmountChange = { amount ->
                        viewModel.onEvent(
                            SearchEvent.OnAmountForFoodChange(
                                food = food.food,
                                amount = amount
                            )
                        )
                    },
                    onTrack = {
                        keyboardController?.hide()
                        viewModel.onEvent(
                            SearchEvent.OnTrackFoodClick(
                                food = food.food,
                                mealType = MealType.fromString(mealName),
                                date = date
                            )
                        )
                    }
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isSearching -> CircularProgressIndicator()
            state.trackableFood.isEmpty() -> {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}