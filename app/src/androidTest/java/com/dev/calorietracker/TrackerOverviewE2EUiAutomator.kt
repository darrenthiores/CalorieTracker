package com.dev.calorietracker

import android.content.Context
import android.content.Intent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.dev.core.domain.model.ActivityLevel
import com.dev.core.domain.model.Gender
import com.dev.core.domain.model.GoalType
import com.dev.core.domain.model.UserInfo
import com.dev.core.domain.preferences.Preferences
import com.dev.tracker_domain.use_case.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

private const val BASIC_SAMPLE_PACKAGE = "com.dev.calorietracker"

@ExperimentalComposeUiApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TrackerOverviewE2EUiAutomator {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var device: UiDevice

    private lateinit var preferences: Preferences

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

        hiltRule.inject()

        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        Assert.assertThat(launcherPackage, CoreMatchers.notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            5000L
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            BASIC_SAMPLE_PACKAGE)?.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            5000L
        )
    }

    @Test
    fun add_breakfast_appears_under_breakfast_and_nutrient_properly_calculated() {
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        device
            .findObject(
                UiSelector()
                    .text("Breakfast")
            )
            .click()

        device
            .findObject(
                UiSelector()
                    .text("Add Breakfast")
            )
            .exists()

        device
            .findObject(
                UiSelector()
                    .text("Add Breakfast")
            )
            .click()

        device
            .findObject(
                By.res("search_textfield")
            )
            .text = "banana"

        device
            .findObject(
                By.res("Search...")
            )
            .click()

        device
            .findObject(
                UiSelector()
                    .text("Carbs")
            )
            .click()

        device
            .findObject(
                By.res("Amount")
            )
            .text = addedAmount.toString()

        device
            .findObject(
                By.res("Track")
            )
            .click()

        device
            .findObject(
                UiSelector()
                    .text(expectedCalories.toString())
            )
            .exists()

        device
            .findObject(
                UiSelector()
                    .text(expectedCarbs.toString())
            )
            .exists()

        device
            .findObject(
                UiSelector()
                    .text(expectedProtein.toString())
            )
            .exists()

        device
            .findObject(
                UiSelector()
                    .text(expectedFat.toString())
            )
            .exists()
    }
}