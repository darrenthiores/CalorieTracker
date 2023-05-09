package com.dev.tracker_data.repository

import com.dev.core.util.Resource
import com.dev.tracker_data.local.source.TrackerLocalDataSource
import com.dev.tracker_data.remote.malformedFoodResponse
import com.dev.tracker_data.remote.service.OpenFoodApi
import com.dev.tracker_data.remote.source.TrackerRemoteDataSource
import com.dev.tracker_data.remote.validFoodResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TrackerRepositoryImplTest {

    private lateinit var repository: TrackerRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: OpenFoodApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient
            .Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(OpenFoodApi::class.java)

        val localDataSource = TrackerLocalDataSource(
            trackerDb = mockk(relaxed = true)
        )
        val remoteDataSource = TrackerRemoteDataSource(
            api = api,
            dispatchers = mockk(relaxed = true)
        )

        repository = TrackerRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Search food, valid response, returns results`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validFoodResponse)
        )

        val result = repository.searchFood(
            query = "banana",
            page = 1,
            pageSize = 40
        )

        assertThat(result is Resource.Success).isTrue()
    }

    @Test
    fun `Search food, invalid response, returns error`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validFoodResponse)
        )

        val result = repository.searchFood(
            query = "banana",
            page = 1,
            pageSize = 40
        )

        assertThat(result is Resource.Error).isTrue()
    }

    @Test
    fun `Search food, malformed response, returns error`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(malformedFoodResponse)
        )

        val result = repository.searchFood(
            query = "banana",
            page = 1,
            pageSize = 40
        )

        assertThat(result is Resource.Error).isTrue()
    }
}