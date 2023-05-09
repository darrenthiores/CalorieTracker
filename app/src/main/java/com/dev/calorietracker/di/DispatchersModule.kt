package com.dev.calorietracker.di

import com.dev.core.domain.dispatchers.DispatchersProvider
import com.dev.core.util.StandardDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatchersModule {

    @Binds
    abstract fun provideDispatchers(
        dispatchers: StandardDispatchers
    ): DispatchersProvider
    
}