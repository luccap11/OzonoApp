package it.luccap11.android.weatherconditions.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.luccap11.android.weatherconditions.WeatherConditionApp
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.data.WeatherDataParser

/**
 * @author Luca Capitoli
 */
class OWeatherMapRepository: WeatherRepository {
    private val baseUrl = "https://api.openweathermap.org/data/2.5/forecast"

    override fun fetchWeatherData(selectedCity: String, completion: (Resource<List<WeatherData>>) -> Unit) {
        val resource = WeatherConditionApp.instance.resources
        val url = String.format("%s?q=%s&APPID=%s&units=metric", baseUrl, selectedCity, resource.getString(R.string.owm_api_key))
        val requestQueue = Volley.newRequestQueue(WeatherConditionApp.instance)
        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                val result = WeatherDataParser.getWeatherLiveData(response)
                completion(Resource.Success(result))
            }) { error ->
                Log.e("TAG", error.toString())
                completion(Resource.Error(resource.getString(R.string.error_label)))
            }
        requestQueue.add(jsonObjectRequest)
    }
}