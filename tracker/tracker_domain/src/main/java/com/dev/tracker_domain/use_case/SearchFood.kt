package com.dev.tracker_domain.use_case

import com.dev.core.util.Resource
import com.dev.tracker_domain.model.TrackableFood
import com.dev.tracker_domain.repository.TrackerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SearchFood @Inject constructor(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40
    ): Resource<List<TrackableFood>> {
        if(query.isBlank()) {
            return Resource.Success(emptyList())
        }

        return repository.searchFood(
            query = query.trim(),
            page = page,
            pageSize = pageSize
        )
    }
}