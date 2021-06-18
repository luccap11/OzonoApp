package it.luccap11.android.ozono.ui.main

import android.content.Intent
import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.util.DataBindingIdlingResource
import it.luccap11.android.ozono.util.EspressoIdlingResource
import it.luccap11.android.ozono.util.TestUtil.typeSearchViewText
import it.luccap11.android.ozono.util.monitorActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFragmentTest {
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun before() {
//        launchInContainer(SearchFragment::class.java, null, R.style.Theme_AppCompat, null)
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activityScenario = ActivityScenario.launch<MainActivity>(intent)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @Test
    fun checkAllViews() {
        onView(withId(R.id.mainSearchFrag)).check(matches(isDisplayed()))
            //.check(matches(hasBackground(R.drawable.round_rectangle)))
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkAllViews_rotate() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
        device.setOrientationLeft()

        onView(withId(R.id.mainSearchFrag)).check(matches(isDisplayed()))
            //.check(matches(hasBackground(R.drawable.round_rectangle)))
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
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
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText(("K")))

        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(isDisplayed()))
    }

    @Test
    fun checkEmptySearchView_rotate() {
//        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
//        onView(withId(R.id.searchView)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText(("K")))

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
        device.setOrientationLeft()

//        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("K")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(isDisplayed()))
    }

    @Test
    fun writeNewCity_wrong() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(typeSearchViewText(""))//clear
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText(("Abcde")))

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("Abcde")))
        onView(withId(R.id.citiesDataLoading)).check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(CoreMatchers.not(isDisplayed())))
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }
}