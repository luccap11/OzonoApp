package it.luccap11.android.ozono.ui.main

import android.content.Intent
import android.view.KeyEvent
import androidx.annotation.UiThread
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.infrastructure.room.AppDatabase
import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity
import it.luccap11.android.ozono.util.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Luca Capitoli
 * Simply sanity test to ensure that activity launches without any issues and shows some data.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    private lateinit var cityEntity: CityEntity
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun before(): Unit = runBlocking {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)

        cityEntity = TestUtil.createCities(1, "Budapest")[0]
        AppDatabase.getInstance().citiesDao().insertCities(cityEntity)
    }

    @Test
    @UiThread
    fun showFragments() {
        launchActivity()

        onView(withId(R.id.container)).check(matches(isDisplayed()))
        onView(withId(R.id.container)).check { _, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
        }
        onView(withId(R.id.search_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.weather_data_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun noLastCitySearched() {
        //reset coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(AppUtils.NOT_SET.toFloat())
        prefsManager.saveLastSearchedCityLongit(AppUtils.NOT_SET.toFloat())

        launchActivity()

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun withLastCitySearched_noDb() {
        //set coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(12.3456f)
        prefsManager.saveLastSearchedCityLongit(12.3456f)

        launchActivity()

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))//depends if city is in cache
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun withLastCitySearched_loading() {
        //set coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(0f)
        prefsManager.saveLastSearchedCityLongit(0f)

        launchActivity()

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("Budapest")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.weatherDataLoading)).check(matches(isDisplayed())) depends if city is in cache
        onView(withId(R.id.emptyWeatherImage)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun withLastCitySearched_success() {
        //set coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(0f)
        prefsManager.saveLastSearchedCityLongit(0f)

        launchActivity()

        Thread.sleep(2000)

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("Budapest")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emptyWeatherImage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(isDisplayed()))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    //TODO spostare
    fun writeNewCity() {
        launchActivity()

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(TestUtil.typeSearchViewText(""))//clear
        onView(withId(R.id.searchView)).perform(TestUtil.typeSearchViewText("Abcde"))
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("Abcde")))
        Thread.sleep(100)
        onView(withId(R.id.citiesDataLoading)).check(matches(isDisplayed()))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
    }

    @Test
    fun searchNewCity() {
        launchActivity()

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(TestUtil.typeSearchViewText(""))//clear
        onView(withId(R.id.searchView)).perform(
            TestUtil.typeSearchViewText("Denver"),
            ViewActions.pressKey(KeyEvent.KEYCODE_SEARCH)
        )
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("Denver")))

        //It depends if the city is in cache or not...
//        Thread.sleep(100)
//        onView(withId(R.id.citiesDataLoading)).check(matches(isDisplayed()))
//        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))

        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emptyWeatherImage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(isDisplayed()))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun searchNewCity_Wrong() {
        //reset coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(AppUtils.NOT_SET.toFloat())
        prefsManager.saveLastSearchedCityLongit(AppUtils.NOT_SET.toFloat())

        launchActivity()

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchView)).perform(TestUtil.typeSearchViewText(""))//clear
        onView(withId(R.id.searchView)).perform(
            TestUtil.typeSearchViewText("xxxxxxx"),
            ViewActions.pressKey(KeyEvent.KEYCODE_ENTER)
        )

        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))
        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultMessage)).check(matches(isDisplayed())).check(matches(withText(R.string.error_label)))
    }

    @After
    fun after(): Unit = runBlocking {
        AppDatabase.getInstance().citiesDao().deleteCityByCoords(cityEntity)

        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    private fun launchActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activityScenario = ActivityScenario.launch<MainActivity>(intent)
        dataBindingIdlingResource.monitorActivity(activityScenario)
    }
}