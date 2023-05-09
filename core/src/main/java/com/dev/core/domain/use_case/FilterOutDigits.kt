package com.dev.core.domain.use_case

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FilterOutDigits @Inject constructor() {

    operator fun invoke(
        text: String
    ): String = text.filter { it.isDigit() }
}