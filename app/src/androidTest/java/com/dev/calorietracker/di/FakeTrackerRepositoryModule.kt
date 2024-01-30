package com.dev.calorietracker.di

import com.dev.calorietracker.repository.FakeTrackerRepository
import com.dev.tracker_data.di.TrackerRepositoryModule
import com.dev.tracker_data.repository.TrackerRepositoryImpl
import com.dev.tracker_domain.repository.TrackerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TrackerRepositoryModule::class]
)
abstract class FakeTrackerRepositoryModule {

    @Binds
    abstract fun provideTrackerRepository(
        repository: FakeTrackerRepository
    ): TrackerRepository
    
}