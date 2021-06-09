package it.luccap11.android.ozono.ui.main

import android.content.Intent
import androidx.annotation.UiThread
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.infrastructure.room.AppDatabase
import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity
import it.luccap11.android.ozono.utils.AppUtils
import it.luccap11.android.ozono.utils.PreferencesManager
import it.luccap11.android.ozono.utils.TestUtil
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

    @Before
    fun before(): Unit = runBlocking {
        cityEntity = TestUtil.createCities(1, "Budapest")[0]
        AppDatabase.getInstance().citiesDao().insertCities(cityEntity)
    }

    @Test
    @UiThread
    fun showFragments() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)

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

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(isDisplayed()))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun withLastCitySearched_noDb() {
        //set coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(12.3456f)
        prefsManager.saveLastSearchedCityLongit(12.3456f)

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(isDisplayed()))

        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(isDisplayed()))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun withLastCitySearched_loading() {
        //set coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(0f)
        prefsManager.saveLastSearchedCityLongit(0f)

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)

        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(withText("Budapest")))
        onView(withId(R.id.citiesDataLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.citiesList)).check(matches(not(isDisplayed())))
        onView(withId(R.id.weatherDataLoading)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyWeatherImage)).check(matches(not(isDisplayed())))
        onView(withId(R.id.listWeatherData)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultMessage)).check(matches(not(isDisplayed())))
    }

    @Test
    fun withLastCitySearched_success() {
        //set coords
        val prefsManager = PreferencesManager()
        prefsManager.saveLastSearchedCityLatit(0f)
        prefsManager.saveLastSearchedCityLongit(0f)

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)

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

    @After
    fun after(): Unit = runBlocking {
        AppDatabase.getInstance().citiesDao().deleteCityByCoords(cityEntity)
    }
}