package com.dev.calorietracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev.calorietracker.navigation.Route
import com.dev.calorietracker.state.AppState
import com.dev.calorietracker.state.rememberAppState
import com.dev.onboarding_presentation.activity.ActivityScreen
import com.dev.onboarding_presentation.age.AgeScreen
import com.dev.onboarding_presentation.gender.GenderScreen
import com.dev.onboarding_presentation.goal.GoalScreen
import com.dev.onboarding_presentation.height.HeightScreen
import com.dev.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.dev.onboarding_presentation.weight.WeightScreen
import com.dev.onboarding_presentation.welcome.WelcomeScreen
import com.dev.tracker_presentation.search.SearchScreen
import com.dev.tracker_presentation.tracker_overview.TrackerOverviewScreen
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@Composable
fun CalorieTracker(
    appState: AppState = rememberAppState(),
    shouldShowOnBoarding: Boolean = true
) {
    val scaffoldState = appState.scaffoldState
    val navController = appState.navController

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = if(shouldShowOnBoarding) Route.WELCOME else Route.TRACKER_OVERVIEW
        ) {
            composable(Route.WELCOME) {
                WelcomeScreen(
                    onNextClick = {
                        navController.navigate(Route.GENDER)
                    }
                )
            }
            composable(Route.GENDER) {
                GenderScreen(
                    onNextClick = {
                        navController.navigate(Route.AGE)
                    }
                )
            }
            composable(Route.AGE) {
                AgeScreen(
                    onNextClick = {
                        navController.navigate(Route.HEIGHT)
                    },
                    showSnackBar = appState::showSnackBar
                )
            }
            composable(Route.HEIGHT) {
                HeightScreen(
                    onNextClick = {
                        navController.navigate(Route.WEIGHT)
                    },
                    showSnackBar = appState::showSnackBar
                )
            }
            composable(Route.WEIGHT) {
                WeightScreen(
                    onNextClick = {
                        navController.navigate(Route.ACTIVITY)
                    },
                    showSnackBar = appState::showSnackBar
                )
            }
            composable(Route.ACTIVITY) {
                ActivityScreen(
                    onNextClick = {
                        navController.navigate(Route.GOAL)
                    }
                )
            }
            composable(Route.GOAL) {
                GoalScreen(
                    onNextClick = {
                        navController.navigate(Route.NUTRIENT_GOAL)
                    }
                )
            }
            composable(Route.NUTRIENT_GOAL) {
                NutrientGoalScreen(
                    onNextClick = {
                        navController.navigate(Route.TRACKER_OVERVIEW)
                    },
                    showSnackBar = appState::showSnackBar
                )
            }
            composable(Route.TRACKER_OVERVIEW) {
                TrackerOverviewScreen(
                    onNavigateToSearch = { mealType, day, month, year ->
                        navController.navigate(
                            route = Route.SEARCH
                                    + "/$mealType"
                                    + "/$day"
                                    + "/$month"
                                    + "/$year"
                        )
                    }
                )
            }
            composable(
                route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                arguments = listOf(
                    navArgument("mealName") {
                        type = NavType.StringType
                    },
                    navArgument("dayOfMonth") {
                        type = NavType.IntType
                    },
                    navArgument("month") {
                        type = NavType.IntType
                    },
                    navArgument("year") {
                        type = NavType.IntType
                    }
                )
            ) {
                val mealName = it.arguments?.getString("mealName")!!
                val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                val month = it.arguments?.getInt("month")!!
                val year = it.arguments?.getInt("year")!!
                val date = LocalDate.of(year, month, dayOfMonth)

                SearchScreen(
                    mealName = mealName,
                    date = date,
                    onNavigateUp = navController::navigateUp,
                    showSnackBar = appState::showSnackBar
                )
            }
        }
    }
}