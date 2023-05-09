package com.dev.tracker_presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.core.R
import com.dev.core.domain.use_case.FilterOutDigits
import com.dev.core.util.Resource
import com.dev.core.util.UiEvent
import com.dev.core.util.UiText
import com.dev.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.OnQueryChange -> {
                _state.value = _state.value.copy(
                    query = event.query
                )
            }
            is SearchEvent.OnAmountForFoodChange -> {
                _state.value = _state.value.copy(
                    trackableFood = _state.value.trackableFood.map { trackableFood ->
                        if(trackableFood.food == event.food) {
                            trackableFood.copy(
                                amount = filterOutDigits(event.amount)
                            )
                        } else {
                            trackableFood
                        }
                    }
                )
            }
            SearchEvent.OnSearch -> {
                executeSearch()
            }
            is SearchEvent.OnSearchFocusChange -> {
                _state.value = _state.value.copy(
                    isHintVisible = !event.isFocus && _state.value.query.isBlank()
                )
            }
            is SearchEvent.OnToggleTrackableFood -> {
                _state.value = _state.value.copy(
                    trackableFood = _state.value.trackableFood.map { trackableFood ->
                        if(trackableFood.food == event.food) {
                            trackableFood.copy(
                                isExpanded = !trackableFood.isExpanded
                            )
                        } else {
                            trackableFood
                        }
                    }
                )
            }
            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = _state.value.trackableFood.find { it.food == event.food }

            trackerUseCases.trackFood(
                food = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )

            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isSearching = true,
                trackableFood = emptyList()
            )

            val result = trackerUseCases
                .searchFood(query = _state.value.query)

            when(result) {
                is Resource.Loading -> Unit
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isSearching = false
                    )

                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        trackableFood = result.data?.let { listOFTrackableFood ->
                            listOFTrackableFood.map { trackableFood ->
                                TrackableFoodUiState(
                                    food = trackableFood
                                )
                            }
                        } ?: emptyList(),
                        isSearching = false,
                        query = ""
                    )
                }
            }
        }
    }
}