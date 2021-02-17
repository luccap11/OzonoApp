package it.luccap11.android.weatherconditions.model.data

import org.junit.Assert
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
        Assert.assertEquals("Madrid", weatherData.city)
        Assert.assertEquals("Sunny", weatherData.descr)
        Assert.assertEquals(24.3f, weatherData.temp)
        Assert.assertEquals("2021-09-12", weatherData.date)
        Assert.assertEquals("icon", weatherData.icon)
        Assert.assertEquals(timeInMillis, weatherData.timeInMillis)
    }

}