package it.luccap11.android.weatherconditions.model.data

import androidx.annotation.NonNull
import org.json.JSONObject

/**
 * @author Luca Capitoli
 */
object WeatherDataBuilder {
    private const val NUM_OF_DETECTIONS_PER_DAY = 8

    fun getWeatherLiveData(@NonNull json: JSONObject): List<WeatherData> {
        val listOfDays = json.getJSONArray("list")
        val result = mutableListOf<WeatherData>()
        for (i in 0 until listOfDays.length() step NUM_OF_DETECTIONS_PER_DAY) {
            val dayWeatherData = listOfDays.getJSONObject(i)
            val weatherObject = dayWeatherData.getJSONArray("weather").getJSONObject(0)
            val descr = weatherObject.getString("main")
            val date = dayWeatherData.getString("dt_txt")
            val icon = weatherObject.getString("icon")
            val temp = dayWeatherData.getJSONObject("main").getDouble("temp")
            result.add(WeatherData(descr, temp.toFloat(), date, icon))
        }
        return result
    }
}