package it.luccap11.android.ozono.ui.main

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.util.DataBindingIdlingResource
import it.luccap11.android.ozono.util.EspressoIdlingResource
import it.luccap11.android.ozono.util.TestUtil.typeSearchViewText
import it.luccap11.android.ozono.util.TestUtil.withViewAtPosition
import it.luccap11.android.ozono.util.monitorActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
//TODo flaky tests
class RecyclerViewCitiesTest {
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun before() {
        //FragmentScenario.launch(SearchFragment::class.java, null, R.style.Theme_AppCompat, null)
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activityScenario = ActivityScenario.launch<MainActivity>(intent)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)

        onView(withId(R.id.searchView)).perform(click())
        onView(withId(R.id.searchView)).perform(typeSearchViewText("Lon"))
    }

    @Test
    fun checkAllViews() {
        onView(withId(R.id.citiesList)).check(matches(isDisplayed()))
        onView(withId(R.id.citiesList)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.divider), isDisplayed())))))
        onView(withId(R.id.citiesList)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.locationImage), isDisplayed())))))
        onView(withId(R.id.citiesList)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.cityDescr), isDisplayed())))))
    }

    @Test(expected = PerformException::class)
    fun scrollTest_Fail() {
        onView(withId(R.id.citiesList))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(20))
    }

    @Test
    fun scrollTest() {
        onView(withId(R.id.citiesList))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }
}