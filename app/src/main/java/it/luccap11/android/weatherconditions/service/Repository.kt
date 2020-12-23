package it.luccap11.android.weatherconditions.service

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.luccap11.android.weatherconditions.App
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.model.Resource
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.data.WeatherDataParser

class Repository {

    fun getRepositories(selectedCity: String, completion: (Resource<List<WeatherData>>) -> Unit) {
        if (selectedCity.isNotBlank()) {
            val resource = App.instance.resources
            val url = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&APPID=%s&units=metric", selectedCity,
                resource.getString(R.string.owm_api_key)
            )
            val requestQueue = Volley.newRequestQueue(App.instance)
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
}