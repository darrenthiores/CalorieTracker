package com.dev.onboarding_presentation.nutrient_goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.core.domain.preferences.Preferences
import com.dev.core.domain.use_case.FilterOutDigits
import com.dev.core.util.UiEvent
import com.dev.onboarding_domain.use_case.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
    private val validateNutrients: ValidateNutrients
): ViewModel() {

    private val _state = MutableStateFlow(NutrientGoalState())
    val state: StateFlow<NutrientGoalState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {
        when(event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {
                _state.value = _state.value.copy(
                    carbsRatio = filterOutDigits(event.ratio)
                )
            }
            is NutrientGoalEvent.OnProteinRatioEnter -> {
                _state.value = _state.value.copy(
                    proteinRatio = filterOutDigits(event.ratio)
                )
            }
            is NutrientGoalEvent.OnFatRatioEnter -> {
                _state.value = _state.value.copy(
                    fatRatio = filterOutDigits(event.ratio)
                )
            }
            NutrientGoalEvent.OnNextClick -> {
                val result = validateNutrients(
                    carbsRatioText = _state.value.carbsRatio,
                    proteinRatioText = _state.value.proteinRatio,
                    fatRatioText = _state.value.fatRatio
                )

                when(result) {
                    is ValidateNutrients.Result.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.ShowSnackBar(result.message))
                        }
                    }
                    is ValidateNutrients.Result.Success -> {
                        preferences.saveCarbRatio(result.carbsRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)

                        preferences.saveShouldShowOnBoarding(false)

                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.Success)
                        }
                    }
                }
            }
        }
    }
}