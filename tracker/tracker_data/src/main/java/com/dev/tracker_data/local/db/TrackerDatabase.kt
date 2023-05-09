package com.dev.tracker_data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.tracker_data.local.dao.TrackerDao
import com.dev.tracker_data.local.entity.TrackedFoodEntity

@Database(
    entities = [TrackedFoodEntity::class],
    version = 1
)
abstract class TrackerDatabase: RoomDatabase() {

    abstract fun trackerDao(): TrackerDao
}