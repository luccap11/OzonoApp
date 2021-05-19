package it.luccap11.android.ozono.model.data

import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Luca Capitoli
 */
class WeatherDataTest {

    @Test
    fun testAllFields() {
        val timeInMillis =  System.currentTimeMillis()
        val weatherData = WeatherData("Madrid", "Sunny", 24.3f, "2021-09-12", "icon", timeInMillis)
        Assert.assertNotEquals(null, weatherData)
        assertThat(weatherData.city, `is`("Madrid"))
        assertThat(weatherData.descr, `is`("Sunny"))
        assertThat(weatherData.temp, `is`(24.3f))
        assertThat(weatherData.date, `is`("2021-09-12"))
        assertThat(weatherData.icon, `is`("icon"))
        assertThat(weatherData.timeInMillis, `is`(timeInMillis))
    }

}