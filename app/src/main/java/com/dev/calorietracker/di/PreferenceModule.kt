package com.dev.calorietracker.di

import com.dev.core.data.preferences.DefaultPreferences
import com.dev.core.domain.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceModule {

    @Binds
    abstract fun providePreferences(
        preferences: DefaultPreferences
    ): Preferences
    
}