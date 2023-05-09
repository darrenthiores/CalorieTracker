package com.dev.calorietracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dev.calorietracker.navigation.Route
import com.dev.calorietracker.repository.FakeTrackerRepository
import com.dev.calorietracker.state.rememberAppState
import com.dev.core.domain.model.ActivityLevel
import com.dev.core.domain.model.Gender
import com.dev.core.domain.model.GoalType
import com.dev.core.domain.model.UserInfo
import com.dev.core.domain.preferences.Preferences
import com.dev.core.domain.use_case.FilterOutDigits
import com.dev.core_ui.ui.theme.CalorieTrackerTheme
import com.dev.tracker_domain.model.TrackableFood
import com.dev.tracker_domain.use_case.*
import com.dev.tracker_presentation.search.SearchScreen
import com.dev.tracker_presentation.search.SearchViewModel
import com.dev.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.dev.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repository: FakeTrackerRepository
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        repository = FakeTrackerRepository()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )

        trackerOverviewViewModel = TrackerOverviewViewModel(
            trackerUseCases = trackerUseCases
        )

        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigits = FilterOutDigits()
        )

        composeRule.activity.setContent {
            CalorieTrackerTheme {
                navController = rememberNavController()
                val appState = rememberAppState(
                    navController = navController
                )
                val scaffoldState = appState.scaffoldState

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    scaffoldState = scaffoldState
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier
                            .padding(paddingValues),
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
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
                                },
                                viewModel = trackerOverviewViewModel
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
                                showSnackBar = appState::showSnackBar,
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun add_breakfast_appears_under_breakfast_and_nutrient_properly_calculated() {
        repository.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbsPer100g = 50,
                proteinPer100g = 5,
                fatPer100g = 1
            )
        )

        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.SEARCH)
        ).isTrue()

        composeRule
            .onNodeWithTag("search_textfield")
            .performTextInput("banana")

        composeRule
            .onNodeWithContentDescription("Search...")
            .performClick()

        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toString())

        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        assertThat(
            navController
                .currentDestination
                ?.route
                ?.equals(Route.TRACKER_OVERVIEW)
        ).isTrue()

        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()
    }
}