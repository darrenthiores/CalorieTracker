package com.dev.tracker_data.repository

import com.dev.core.data.util.ApiResponse
import com.dev.core.util.Resource
import com.dev.tracker_data.local.source.TrackerLocalDataSource
import com.dev.tracker_data.mapper.toTrackableFood
import com.dev.tracker_data.mapper.toTrackedFood
import com.dev.tracker_data.mapper.toTrackedFoodEntity
import com.dev.tracker_data.remote.source.TrackerRemoteDataSource
import com.dev.tracker_domain.model.TrackableFood
import com.dev.tracker_domain.model.TrackedFood
import com.dev.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRepositoryImpl @Inject constructor(
    private val remoteDataSource: TrackerRemoteDataSource,
    private val localDataSource: TrackerLocalDataSource
): TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Resource<List<TrackableFood>> =
        when(
            val response = remoteDataSource
                .searchFood(
                    query = query,
                    page = page,
                    pageSize = pageSize
                )
        ) {
            ApiResponse.Empty -> {
                Resource.Error("Unexpected Error Happen!")
            }
            is ApiResponse.Error -> {
                Resource.Error(response.errorMessage)
            }
            is ApiResponse.Success -> {
                Resource.Success(
                    response.data.products
                        .filter {
                            val calculatedCalories = it.nutriments.carbohydrates100g * 4f +
                                    it.nutriments.proteins100g * 4f +
                                    it.nutriments.fat100g * 9f

                            val lowerBound = calculatedCalories * 0.99f
                            val upperBound = calculatedCalories * 1.01f

                            it.nutriments.energyKcal100g in (lowerBound..upperBound)
                        }
                        .mapNotNull {
                            it.toTrackableFood()
                        }
                )
            }
        }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        localDataSource.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        localDataSource.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> =
        localDataSource.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { entity ->
                entity.toTrackedFood()
            }
        }
}