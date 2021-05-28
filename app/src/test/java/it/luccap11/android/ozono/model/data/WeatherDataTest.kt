package it.luccap11.android.ozono.model.data

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Test

/**
 * @author Luca Capitoli
 */
class WeatherDataTest {

    @Test
    fun testAllFields() {
        val timeInMillis =  System.currentTimeMillis()
        val weatherData = WeatherData(listOf(ListData(
            timeInMillis, listOf(Weather("icon")), Main(23.5f))),
            City("Sulmona")
        )
        Assert.assertNotEquals(null, weatherData)
        assertThat(weatherData.city.name, `is`("Sulmona"))
        assertThat(weatherData.list[0].main.temp, `is`(23.5f))
        assertThat(weatherData.list[0].weather[0].icon, `is`("icon"))
        assertThat(weatherData.list[0].timeInSecs, `is`(timeInMillis))
    }

}