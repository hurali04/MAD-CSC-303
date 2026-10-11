package com.example.devicereadiness

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceReadinessScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun blankForm_showsBothValidationMessages() {
        val activity = composeRule.activity
        composeRule.onNodeWithTag("evaluateButton").performClick()
        composeRule.onNodeWithText(
            activity.getString(R.string.error_device_name_required),
        ).assertIsDisplayed()
        composeRule.onNodeWithText(
            activity.getString(R.string.error_battery_invalid),
        ).assertIsDisplayed()
    }

    @Test
    fun validForm_showsReadyResult() {
        val activity = composeRule.activity
        composeRule.onNodeWithTag("deviceNameInput")
            .performTextInput("Sensor-A")
        composeRule.onNodeWithTag("batteryInput")
            .performTextInput("80")
        composeRule.onNodeWithTag("evaluateButton").performClick()
        composeRule.onNodeWithText(
            activity.getString(R.string.result_ready, "Sensor-A"),
        ).assertIsDisplayed()
    }
}
