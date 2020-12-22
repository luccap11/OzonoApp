package it.luccap11.android.weatherconditions.model.builder

import it.luccap11.android.weatherconditions.model.data.WeatherData
import org.json.JSONObject

/**
 * @author Luca Capitoli
 */
object WeatherDataBuilder {
    fun getWeatherLiveData(json: JSONObject): List<WeatherData> {
        val listOfDays = json.getJSONArray("list")
        val result = mutableListOf<WeatherData>()
        for (i in 0 until listOfDays.length() step 8) {
            val descr = listOfDays.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")
            val date = listOfDays.getJSONObject(i).getString("dt_txt")
            val icon = listOfDays.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
            val temp = listOfDays.getJSONObject(i).getJSONObject("main").getDouble("temp")
            result.add(WeatherData(descr, temp.toFloat(), date, icon))
        }
        return result
    }
}