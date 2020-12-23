package it.luccap11.android.weatherconditions.model

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.model.data.WeatherDataParser
import it.luccap11.android.weatherconditions.model.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val liveData = MutableLiveData<Resource<List<WeatherData>>>()

    fun getData(@NonNull selectedCity: String) {
        liveData.postValue(Resource.Loading())
        if (selectedCity.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val resource = getApplication<Application>().resources
                val url = String.format(
                    "https://api.openweathermap.org/data/2.5/forecast?q=%s&APPID=%s", selectedCity,
                    resource.getString(R.string.owm_api_key)
                )
                val requestQueue = Volley.newRequestQueue(getApplication())
                val jsonObjectRequest =
                    JsonObjectRequest(Request.Method.GET, url, null, { response ->
                        val result = WeatherDataParser.getWeatherLiveData(response)
                        liveData.postValue(Resource.Success(result))
                    }) { error ->
                        Log.e("TAG", error.toString())
                        liveData.postValue(Resource.Error(resource.getString(R.string.error_label)))
                    }

                requestQueue.add(jsonObjectRequest)
            }
        }
    }
}