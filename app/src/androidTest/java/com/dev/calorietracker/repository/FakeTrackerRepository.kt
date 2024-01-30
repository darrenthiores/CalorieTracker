package com.dev.calorietracker.repository

import com.dev.core.util.Resource
import com.dev.tracker_domain.model.TrackableFood
import com.dev.tracker_domain.model.TrackedFood
import com.dev.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeTrackerRepository @Inject constructor() : TrackerRepository {

    var shouldReturnError = false

    private val trackedFood = mutableListOf<TrackedFood>()
    var searchResults = listOf(
        TrackableFood(
            name = "banana",
            imageUrl = null,
            caloriesPer100g = 150,
            carbsPer100g = 50,
            proteinPer100g = 5,
            fatPer100g = 1
        )
    )

    private val getFoodsForDateFlow = MutableSharedFlow<List<TrackedFood>>(
        replay = 1
    )

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Resource<List<TrackableFood>> {
        return if(shouldReturnError) {
            Resource.Error("Error")
        } else {
            Resource.Success(searchResults)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackedFood.add(food.copy(id = Random.nextInt()))
        getFoodsForDateFlow.emit(trackedFood)
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackedFood.remove(food)
        getFoodsForDateFlow.emit(trackedFood)
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}