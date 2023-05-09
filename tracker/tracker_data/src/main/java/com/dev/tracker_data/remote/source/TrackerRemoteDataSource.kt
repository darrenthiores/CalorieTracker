package com.dev.tracker_data.remote.source

import com.dev.core.data.util.ApiResponse
import com.dev.core.domain.dispatchers.DispatchersProvider
import com.dev.tracker_data.remote.dto.SearchDto
import com.dev.tracker_data.remote.service.OpenFoodApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRemoteDataSource @Inject constructor(
    private val api: OpenFoodApi,
    private val dispatchers: DispatchersProvider
) {
    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): ApiResponse<SearchDto> =
        suspendGetResponse {
            val response = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )

            ApiResponse.Success(response)
        }

    private fun <T> getResponse(
        httpCall: suspend () -> ApiResponse<T>
    ): Flow<ApiResponse<T>> = flow {
        try {
            emit(httpCall())
        }
        catch (e: HttpException) {
            emit(ApiResponse.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
        catch (e: IOException) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Couldn't reach server. Check your internet connection."))
        }
        catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }.flowOn(dispatchers.io)

    private suspend fun <T> suspendGetResponse(
        httpCall: suspend () -> ApiResponse<T>
    ): ApiResponse<T> =
        try {
            httpCall()
        }
        catch (e: HttpException) {
            ApiResponse.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
        catch (e: IOException) {
            ApiResponse.Error(e.localizedMessage ?: "Couldn't reach server. Check your internet connection.")
        }
        catch (e: Exception) {
            ApiResponse.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
}