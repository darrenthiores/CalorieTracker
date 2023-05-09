package com.dev.tracker_data.di

import com.dev.tracker_data.repository.TrackerRepositoryImpl
import com.dev.tracker_domain.repository.TrackerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackerRepositoryModule {

    @Binds
    abstract fun provideTrackerRepository(
        repository: TrackerRepositoryImpl
    ): TrackerRepository
    
}