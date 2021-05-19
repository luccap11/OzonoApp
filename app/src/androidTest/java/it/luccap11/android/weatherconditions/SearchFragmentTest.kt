package it.luccap11.android.weatherconditions

import android.view.KeyEvent
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.testing.FragmentScenario.Companion.launchInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import it.luccap11.android.weatherconditions.ui.main.SearchFragment
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFragmentTest {
    @Before
    fun before() {
        launchInContainer(SearchFragment::class.java, null, R.style.Theme_AppCompat, null)
    }

    @Test
    fun checkAllViews() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.weatherDataLoading))
        onView(withId(R.id.citiesList))
        onView(withId(R.id.emptyWeatherImage))
        onView(withId(R.id.listWeatherData))
    }

    @Test
    fun checkSearchView() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(click())
        onView(withId(R.id.searchView)).perform(typeSearchViewText("London"), pressKey(KeyEvent.KEYCODE_ENTER))
    }

    @Test
    fun checkEmptySearchView() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(click())
        onView(withId(R.id.searchView)).perform(typeSearchViewText(""), pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.searchView)).perform(typeSearchViewText("London"), pressKey(KeyEvent.KEYCODE_ENTER))
    }

    fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Change view text"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }
}