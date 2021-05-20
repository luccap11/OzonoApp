package it.luccap11.android.ozono.network

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.OzonoAppl
import it.luccap11.android.ozono.infrastructure.Resource
import org.json.JSONObject

/**
 * @author Luca Capitoli
 * @since 30/dec/2020
 */
object RemoteWeatherDataSource {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/forecast"

    fun fetchOWeatherMapData(selectedCity: String, completion: (Resource<JSONObject>) -> Unit) {
        val resource = OzonoAppl.instance.resources
        val url = String.format("%s?q=%s&APPID=%s&units=metric", BASE_URL, selectedCity, resource.getString(R.string.owm_api_key))
        val requestQueue = Volley.newRequestQueue(OzonoAppl.instance)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            completion(Resource.Success(response))
        }) { error ->
            Log.e("TAG", error.toString())
            completion(Resource.Error(""))
        }
        requestQueue.add(jsonObjectRequest)
    }
}