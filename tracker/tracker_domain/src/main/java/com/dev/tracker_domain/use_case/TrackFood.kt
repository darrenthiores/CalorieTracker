package com.dev.tracker_domain.use_case

import com.dev.tracker_domain.model.MealType
import com.dev.tracker_domain.model.TrackableFood
import com.dev.tracker_domain.model.TrackedFood
import com.dev.tracker_domain.repository.TrackerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@ViewModelScoped
class TrackFood @Inject constructor(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate
    ) {
        val totalCarbs = ((food.carbsPer100g / 100f) * amount).roundToInt()
        val totalProtein = ((food.proteinPer100g / 100f) * amount).roundToInt()
        val totalFat = ((food.fatPer100g / 100f) * amount).roundToInt()
        val totalCalories = ((food.caloriesPer100g / 100f) * amount).roundToInt()

        repository.insertTrackedFood(
            TrackedFood(
                name = food.name,
                carbs = totalCarbs,
                protein = totalProtein,
                fat = totalFat,
                imageUrl = food.imageUrl,
                mealType = mealType,
                amount = amount,
                date = date,
                calories = totalCalories
            )
        )
    }
}