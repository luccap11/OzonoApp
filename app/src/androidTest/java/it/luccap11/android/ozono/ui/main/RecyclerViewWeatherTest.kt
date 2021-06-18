package it.luccap11.android.ozono.ui.main

import android.content.Intent
import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.util.*
import it.luccap11.android.ozono.util.TestUtil.withViewAtPosition
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class RecyclerViewWeatherTest {
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
//        val scenario = FragmentScenario.launchInContainer(WeatherDataFragment::class.java, null,
//            R.style.Theme_AppCompat, null)

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activityScenario = ActivityScenario.launch<MainActivity>(intent)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)

//        onView(withId(R.id.searchView)).perform(ViewActions.click())
        onView(withId(R.id.searchView)).perform(TestUtil.typeSearchViewText("London"),
            ViewActions.pressKey(KeyEvent.KEYCODE_ENTER)
        )
    }

    @Test
    fun checkAllViews() {
        onView(withId(R.id.listWeatherData)).check(matches(isDisplayed()))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.todayLabel), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.todayText), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.todayLabel), not(isDisplayed()))))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.todayText), not(isDisplayed()))))))

        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.day), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.month), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.dayOfTheWeek), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.weatherImage), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.temperature), isDisplayed())))))
    }

    @Test
    fun checkAllViews_rotate() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationNatural()
        device.setOrientationLeft()
        device.setOrientationNatural()

        onView(withId(R.id.listWeatherData)).check(matches(isDisplayed()))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.todayLabel), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.todayText), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.todayLabel), not(isDisplayed()))))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.todayText), not(isDisplayed()))))))

        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.day), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.month), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.dayOfTheWeek), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.weatherImage), isDisplayed())))))
        onView(withId(R.id.listWeatherData)).check(matches(withViewAtPosition(1, hasDescendant(allOf(withId(R.id.temperature), isDisplayed())))))
    }

//    @Test(expected = PerformException::class) flaky test
//    fun scrollTest_Fail() {
//        onView(withId(R.id.listWeatherData))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(20))
//    }

    @Test
    fun scrollTest() {
        onView(withId(R.id.listWeatherData))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

}