package it.luccap11.android.weatherconditions.model.viewmodels

import it.luccap11.android.weatherconditions.infrastructure.OWeatherMapRepository
import it.luccap11.android.weatherconditions.infrastructure.Resource
import it.luccap11.android.weatherconditions.infrastructure.WorldCitiesRepository
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.Country
import it.luccap11.android.weatherconditions.model.data.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


/**
 * @author Luca Capitoli
 * @since 02/mar/2021
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val citiesRepository =  mock(WorldCitiesRepository::class.java)
    private val weatherRepository =  mock(OWeatherMapRepository::class.java)

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<Resource<List<CityData>>>

    @Test
    fun test() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val subject = WeatherViewModel(citiesRepository, weatherRepository)
        val cityData = CityData("a", Country("b"), 1, "c", Location(0f, 0f))
        `when`(citiesRepository.getLastCitySearched { any() }).then { subject.lastCitySearched.postValue(cityData) }.thenAnswer {
            val argument = it.getArgument<((CityData?) -> Unit)>(0)
            val completion = argument as ((CityData?) -> Unit)
            completion.invoke(cityData) }

        doAnswer { invocation ->
            val args = invocation.arguments
            System.out.println("called with arguments: " + Arrays.toString(args))
            null
        }.`when`(citiesRepository).getLastCitySearched { any() }

        // Checks mockObject called the hashCode method that is expected from the coroutine created in sampleMethod
        subject.getLastCitySearched()
        //verify(citiesRepository).getLastCitySearched { any() }
        delay(3000)
        subject.lastCitySearched.observeForever {  }
        assert(subject.lastCitySearched.value == cityData)
    }
}