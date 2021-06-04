package it.luccap11.android.ozono.model.viewmodels

import it.luccap11.android.ozono.CoroutinesTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.luccap11.android.ozono.TestUtil
import it.luccap11.android.ozono.model.ApiStatus
import it.luccap11.android.ozono.repository.WeatherDataRepository
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.utils.PreferencesManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WeatherViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    private val cityRepository = mock(WorldCitiesRepository::class.java)
    private val weatherRepository = mock(WeatherDataRepository::class.java)
    private lateinit var sharedViewModel: WeatherViewModel

    @Mock
    val prefs = mock(PreferencesManager::class.java)

    @Before
    fun setupViewModel() {
        sharedViewModel = WeatherViewModel(cityRepository, weatherRepository)
    }

    @Test
    fun updateWeatherDataTest_liveData_loading() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            sharedViewModel.updateWeatherData("London")

            val statusValue = sharedViewModel.weatherStatus.getOrAwaitValue()
            val weatherData = sharedViewModel.weatherData
            assertThat(statusValue, `is`(instanceOf(ApiStatus.LOADING::class.java)))
            assertEquals(weatherData.value, null)
        }

    @Test
//    @Ignore("Github launches a TimeoutException")
    fun updateWeatherDataTest_liveData_error() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(weatherRepository.fetchWeatherDataByCityName(anyString()))
            .thenReturn(null)

        sharedViewModel.updateWeatherData("London")

        val statusValue = sharedViewModel.weatherStatus.getOrAwaitValue(2)
        val weatherData = sharedViewModel.weatherData
        assertThat(statusValue, `is`(instanceOf(ApiStatus.ERROR::class.java)))
        assertEquals(weatherData.value, null)
    }

    @Test
//    @Ignore("Github launches a TimeoutException")
    fun updateWeatherDataTest_emptyLiveData_success() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            `when`(weatherRepository.fetchWeatherDataByCityName(anyString()))
                .thenReturn(listOf())

            sharedViewModel.updateWeatherData("London")

            val statusValue = sharedViewModel.weatherStatus.getOrAwaitValue(2)
            val weatherData = sharedViewModel.weatherData
            assertThat(statusValue, `is`(instanceOf(ApiStatus.SUCCESS::class.java)))
            assertThat(weatherData.value, not(nullValue()))
            assertThat(weatherData.value, `is`(emptyList()))
        }

    @Test
//    @Ignore("Github launches a TimeoutException")
    fun updateWeatherDataTest_liveData_success() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            `when`(weatherRepository.fetchWeatherDataByCityName(anyString()))
                .thenReturn(listOf(TestUtil().mockWeatherData(System.currentTimeMillis())))

            sharedViewModel.updateWeatherData("London")

            val statusValue = sharedViewModel.weatherStatus.getOrAwaitValue(2)
            val weatherData = sharedViewModel.weatherData
            assertThat(statusValue, `is`(instanceOf(ApiStatus.SUCCESS::class.java)))
            assertThat(weatherData.value, not(nullValue()))
            assertThat(weatherData.value, not(emptyList()))
            assertEquals(weatherData.value!!.size, 1)
        }

    @Test
    fun updateCityDataTest_liveData_loading() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(cityRepository.fetchLocalCitiesData(anyString(), anyInt()))
            .thenReturn(emptyList())

        sharedViewModel.updateCityData("London")

        val statusValue = sharedViewModel.citiesStatus.getOrAwaitValue()
        val city = sharedViewModel.citiesData
        assertThat(statusValue, `is`(instanceOf(ApiStatus.LOADING::class.java)))
        assertEquals(city.value, null)
    }

    @Test
//    @Ignore("Github launches a TimeoutException")
    fun updateCityDataTest_liveData_error() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(cityRepository.fetchLocalCitiesData(anyString(), anyInt()))
            .thenReturn(emptyList())
        `when`(cityRepository.fetchRemoteCitiesData(anyString(), anyInt()))
            .thenReturn(null)

        sharedViewModel.updateCityData("London")

        val statusValue = sharedViewModel.citiesStatus.getOrAwaitValue(2)
        val city = sharedViewModel.citiesData
        assertThat(statusValue, `is`(instanceOf(ApiStatus.ERROR::class.java)))
        assertEquals(city.value, null)
    }

    @Test
//    @Ignore("Github launches a TimeoutException")
    fun updateCityDataTest_emptyLiveData_success() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(cityRepository.fetchLocalCitiesData(anyString(), anyInt()))
            .thenReturn(emptyList())
        `when`(cityRepository.fetchRemoteCitiesData(anyString(), anyInt()))
            .thenReturn(listOf())

        sharedViewModel.updateCityData("London")

        val statusValue = sharedViewModel.citiesStatus.getOrAwaitValue(2)
        val city = sharedViewModel.citiesData
        assertThat(statusValue, `is`(instanceOf(ApiStatus.SUCCESS::class.java)))
        assertThat(city.value, not(nullValue()))
        assertThat(city.value, `is`(emptyList()))
    }

    @Test
//    @Ignore("Github launches a TimeoutException")
    fun updateCityDataTest_liveData_success() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(cityRepository.fetchRemoteCitiesData(anyString(), anyInt()))
            .thenReturn(listOf(TestUtil().mockCityData()))
        `when`(cityRepository.fetchLocalCitiesData(anyString(), anyInt()))
            .thenReturn(emptyList())

        sharedViewModel.updateCityData("London")

        val statusValue = sharedViewModel.citiesStatus.getOrAwaitValue(2)
        val city = sharedViewModel.citiesData
        assertThat(statusValue, `is`(instanceOf(ApiStatus.SUCCESS::class.java)))
        assertThat(city.value, not(nullValue()))
        assertThat(city.value, not(emptyList()))
        assertEquals(city.value!!.size, 1)
    }

    @Test
    fun getLastCitySearchedTest_cachedData_null() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(cityRepository.getLastCitySearched()).thenReturn(null)

        sharedViewModel.getLastCitySearched()

        val value = sharedViewModel.lastCitySearched.getOrAwaitValue()
        assertEquals(value, null)
    }

    @Test
    fun getLastCitySearchedTest_cachedData_success() = coroutinesTestRule.testDispatcher.runBlockingTest {
//        `when`(prefs.getLastSearchedCityLatit()).thenReturn(12.3456f)
//        `when`(prefs.getLastSearchedCityLongit()).thenReturn(12.3456f)
        `when`(cityRepository.getLastCitySearched()).thenReturn(TestUtil().mockCityData())

        sharedViewModel.getLastCitySearched()

        val value = sharedViewModel.lastCitySearched.getOrAwaitValue()
        assertFalse(value == null)
        assertThat(value.localeNames.cityNames[0], `is`("London"))
        assertThat(value.country.name, `is`("UK"))
        assertThat(value.region, not(emptyList()))
        assertThat(value.region[0], `is`("region"))
        assertThat(value.geoloc.lat, `is`(12.3456f))
        assertThat(value.geoloc.lng, `is`(12.3456f))
    }
}