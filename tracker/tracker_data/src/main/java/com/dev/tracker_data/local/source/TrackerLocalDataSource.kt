package com.dev.tracker_data.local.source

import com.dev.tracker_data.local.db.TrackerDatabase
import com.dev.tracker_data.local.entity.TrackedFoodEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerLocalDataSource @Inject constructor(
    private val trackerDb: TrackerDatabase
) {
    private val trackerDao = trackerDb.trackerDao()

   suspend fun insertTrackedFood(
       trackedFoodEntity: TrackedFoodEntity
   ) {
       trackerDao.insertTrackedFood(trackedFoodEntity)
   }

    suspend fun deleteTrackedFood(
        trackedFoodEntity: TrackedFoodEntity
    ) {
        trackerDao.deleteTrackedFood(trackedFoodEntity)
    }

    fun getFoodsForDate(
        day: Int,
        month: Int,
        year: Int
    ): Flow<List<TrackedFoodEntity>> =
        trackerDao.getFoodsForDate(
            day = day,
            month = month,
            year = year
        )
}