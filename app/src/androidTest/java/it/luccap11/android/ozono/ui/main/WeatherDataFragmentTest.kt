package it.luccap11.android.ozono.ui.main

import androidx.fragment.app.testing.FragmentScenario.Companion.launchInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import it.luccap11.android.ozono.R
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class WeatherDataFragmentTest {

    @Before
    fun before() {
        launchInContainer(WeatherDataFragment::class.java, null, R.style.Theme_AppCompat, null)
    }

    @Test
    fun checkAllStartingViews() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkAllStartingViews_rotateDevice() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
        device.setOrientationLeft()
        device.setOrientationNatural()

        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
    }

}