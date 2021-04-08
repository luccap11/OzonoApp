package it.luccap11.android.weatherconditions.model.data

import it.luccap11.android.weatherconditions.model.WeatherDataJsonResponseExample
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.Is
import org.json.JSONObject
import org.junit.Assert
import org.junit.Assert.assertThat
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
    private val currentTime = System.currentTimeMillis() / 1000

    @Before
    fun before() {
        jsonResponse = WeatherDataJsonResponseExample.fetchJson(currentTime)
    }

    @Test
    fun getWeatherLiveData() {
        val jsonObject = JSONObject(jsonResponse)
        val weatherData = weatherDataParser.getWeatherLiveData(jsonObject)
        assertThat(weatherData.size, `is`(1))
        assertThat(weatherData[0].city, `is`("London"))
        assertThat(weatherData[0].descr, `is`("Clouds"))
        assertThat(weatherData[0].date, `is`("2020-12-24 12:00:00"))
        assertThat(weatherData[0].icon, `is`("03d"))
        assertThat(weatherData[0].temp, `is`(277.93f))
        assertThat(weatherData[0].timeInMillis, `is`(currentTime * 1000))
    }

    @Test
    fun getEmptyWeatherLiveData() {
        val jsonObject = JSONObject()
        val weatherData = weatherDataParser.getWeatherLiveData(jsonObject)
        assertThat(weatherData.size, `is`(0))
    }
}