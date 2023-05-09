package com.dev.tracker_presentation.tracker_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases
): ViewModel() {

    private val _state = MutableStateFlow(TrackerOverviewState())
    val state: StateFlow<TrackerOverviewState> = _state.asStateFlow()

    private var getFoodsForDateJob: Job? = null

    init {
        refreshFoods()
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when(event) {
            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    trackerUseCases
                        .deleteTrackedFood(event.trackedFood)
                }

                refreshFoods()
            }
            TrackerOverviewEvent.OnNextDayClick -> {
                _state.value = _state.value.copy(
                    date = _state.value.date.plusDays(1)
                )

                refreshFoods()
            }
            TrackerOverviewEvent.OnPreviousDayClick -> {
                _state.value = _state.value.copy(
                    date = _state.value.date.minusDays(1)
                )

                refreshFoods()
            }
            is TrackerOverviewEvent.OnToggleMealClick -> {
                _state.value = _state.value.copy(
                    meals = _state.value.meals.map { meal ->
                        if(meal.name == event.meal.name) {
                            meal.copy(
                                isExpanded = !meal.isExpanded
                            )
                        } else {
                            meal
                        }
                    }
                )
            }
        }
    }

    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()
        getFoodsForDateJob = trackerUseCases
            .getFoodsForDate(_state.value.date)
            .onEach { foods ->
                val nutrientsResult = trackerUseCases
                    .calculateMealNutrients(foods)

                _state.value = _state.value.copy(
                    totalCarbs = nutrientsResult.totalCarbs,
                    totalProtein = nutrientsResult.totalProtein,
                    totalFat = nutrientsResult.totalFat,
                    totalCalories = nutrientsResult.totalCalories,
                    carbsGoal = nutrientsResult.carbsGoal,
                    proteinGoal = nutrientsResult.proteinGoal,
                    fatGoal = nutrientsResult.fatGoal,
                    caloriesGoal = nutrientsResult.caloriesGoal,
                    trackedFood = foods,
                    meals = _state.value.meals.map { meal ->
                        val nutrientsForMeal = nutrientsResult
                            .mealNutrients[meal.mealType] ?:
                            return@map meal.copy(
                                carbs = 0,
                                protein = 0,
                                fat = 0,
                                calories = 0
                            )

                        meal.copy(
                            carbs = nutrientsForMeal.carbs,
                            protein = nutrientsForMeal.protein,
                            fat = nutrientsForMeal.fat,
                            calories = nutrientsForMeal.calories
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}