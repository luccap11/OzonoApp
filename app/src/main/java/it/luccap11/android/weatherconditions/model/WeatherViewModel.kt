package it.luccap11.android.weatherconditions.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val liveData = MutableLiveData<WeatherData>()

    fun getData(selectedCity: String): LiveData<WeatherData> {
        if (!selectedCity.isBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val url =
                    "https://api.openweathermap.org/data/2.5/forecast?q=" + selectedCity + "&APPID=7d85604d75067f7a0da53ac8f70c5364"
                val jsonResponses: MutableList<String> = ArrayList()
//
                val requestQueue = Volley.newRequestQueue(getApplication())
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                        Log.d("TAG", response.toString())
                        val city = response!!.getJSONObject("city").getString("name")

                        val descr = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main")
                        val icon = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")
                        val temp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("temp")
                        liveData.postValue(WeatherData(city, descr, temp.toFloat(), icon))
                    }) { error ->
                        Log.e("TAG", error.toString())
                        liveData.postValue(null)
                    }

                requestQueue.add(jsonObjectRequest)
            }
        }
        return liveData
    }
}