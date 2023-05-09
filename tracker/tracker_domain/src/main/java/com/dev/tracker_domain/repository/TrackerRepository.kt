package com.dev.tracker_domain.repository

import com.dev.core.util.Resource
import com.dev.tracker_domain.model.TrackableFood
import com.dev.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrackerRepository {

    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Resource<List<TrackableFood>>

    suspend fun insertTrackedFood(
        food: TrackedFood
    )

    suspend fun deleteTrackedFood(
        food: TrackedFood
    )

    fun getFoodsForDate(
        localDate: LocalDate
    ): Flow<List<TrackedFood>>
}