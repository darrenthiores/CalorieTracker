package com.dev.tracker_domain.use_case

import com.dev.tracker_domain.model.TrackedFood
import com.dev.tracker_domain.repository.TrackerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteTrackedFood @Inject constructor(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        food: TrackedFood
    ) {
        repository
            .deleteTrackedFood(food)
    }
}