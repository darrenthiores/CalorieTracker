package com.dev.core.domain.model

sealed class ActivityLevel(val name: String) {
    object Low: ActivityLevel("low")
    object Medium: ActivityLevel("medium")
    object High: ActivityLevel("high")

    companion object {
        fun fromString(name: String): ActivityLevel =
            when(name) {
                "low" -> Low
                "medium" -> Medium
                "high" -> High
                else -> Low
            }
    }
}
