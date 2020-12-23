package it.luccap11.android.weatherconditions

import android.view.KeyEvent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import it.luccap11.android.weatherconditions.ui.main.MainFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainFragmentTest {
    @Before
    fun before() {
        launchFragmentInContainer<MainFragment>()
    }

    @Test
    fun checkAllViews() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.progressLoading))
        onView(withId(R.id.citiesListView))
        onView(withId(R.id.text))
        onView(withId(R.id.listWeatherData))
    }

    @Test
    fun checkSearchView() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(click())
        onView(withId(R.id.searchView)).perform(typeText("London"), pressKey(KeyEvent.KEYCODE_ENTER))
    }
}