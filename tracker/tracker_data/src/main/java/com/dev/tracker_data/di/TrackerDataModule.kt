package com.dev.tracker_data.di

import android.content.Context
import androidx.room.Room
import com.dev.tracker_data.local.db.TrackerDatabase
import com.dev.tracker_data.remote.service.OpenFoodApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(
                        HttpLoggingInterceptor
                            .Level.BODY
                    )
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideOpenFoodApi(
        client: OkHttpClient
    ): OpenFoodApi =
        Retrofit.Builder()
            .baseUrl(OpenFoodApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(OpenFoodApi::class.java)

    @Singleton
    @Provides
    fun provideTrackerDatabase(
        @ApplicationContext context: Context
    ): TrackerDatabase =
        Room
            .databaseBuilder(
                context,
                TrackerDatabase::class.java,
                "tracker.db"
            )
            .fallbackToDestructiveMigration()
            .build()
}