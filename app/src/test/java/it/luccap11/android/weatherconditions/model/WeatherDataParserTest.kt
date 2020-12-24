package it.luccap11.android.weatherconditions.model

import it.luccap11.android.weatherconditions.model.data.WeatherDataParser
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Luca Capitoli
 */
@RunWith(JUnit4::class)
class WeatherDataParserTest {
    private lateinit var jsonResponse: String
    private val weatherDataParser = WeatherDataParser

    @Before
    fun before() {
        jsonResponse = JsonResponseExample.fetchJson()
    }

    @Test
    fun getWeatherLiveData() {
        val jsonObject = JSONObject(jsonResponse)
        val weatherData = weatherDataParser.getWeatherLiveData(jsonObject)
        Assert.assertEquals(1, weatherData.size)
        Assert.assertEquals("Clouds", weatherData[0].descr)
        Assert.assertEquals("2020-12-24 12:00:00", weatherData[0].date)
        Assert.assertEquals("03d", weatherData[0].icon)
        Assert.assertEquals(277.93f, weatherData[0].temp)
    }

    @Test
    fun getEmptyWeatherLiveData() {
        val jsonObject = JSONObject()
        val weatherData = weatherDataParser.getWeatherLiveData(jsonObject)
        Assert.assertEquals(0, weatherData.size)
    }
}