package com.dev.tracker_domain.use_case

import com.dev.tracker_domain.model.TrackedFood
import com.dev.tracker_domain.repository.TrackerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

@ViewModelScoped
class GetFoodsForDate @Inject constructor(
    private val repository: TrackerRepository
) {

    operator fun invoke(
        date: LocalDate
    ): Flow<List<TrackedFood>> =
        repository.getFoodsForDate(
            localDate = date
        )
}