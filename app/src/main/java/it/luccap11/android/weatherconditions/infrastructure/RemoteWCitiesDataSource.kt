package it.luccap11.android.weatherconditions.infrastructure

import android.util.Log
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.WeatherConditionApp
import it.luccap11.android.weatherconditions.infrastructure.RetrofitClientInstance.retrofitInstance
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WorldCitiesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder


/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
object RemoteWCitiesDataSource {

    fun fetchBack4AppData(selectedCity: String, completion: (Resource<List<CityData>>) -> Unit) {
        val service: WorldCitiesWS = retrofitInstance!!.create(WorldCitiesWS::class.java)
        val where = URLEncoder.encode("""
    {
        "name": {
            "${'$'}gte": "$selectedCity"
        },
        "country": {
            "${'$'}exists": true
        },
        "population": {
            "${'$'}gt": 1000
        }
    }
    """.trimIndent(), "utf-8")
        val call: Call<WorldCitiesData> = service.getCities(where)
        call.enqueue(object : Callback<WorldCitiesData> {
            override fun onResponse(call: Call<WorldCitiesData>, response: Response<WorldCitiesData>) {
                if (findErrorsInBodyResponse(response, response.body())) {
                    completion(Resource.Error(""))
                } else {
                    completion(Resource.Success(response.body()!!.cities))
                }
            }

            override fun onFailure(call: Call<WorldCitiesData>, t: Throwable?) {
                completion(Resource.Error(""))
            }
        })
    }

    private fun findErrorsInBodyResponse(response: Response<WorldCitiesData>, body: WorldCitiesData?): Boolean {
        if (body == null) {
            Log.e(
                WeatherConditionApp.instance.getString(R.string.error_log_tag),
                response.message()
            )

            val errorBody = response.errorBody()
            if (errorBody != null) {
                Log.e(
                    WeatherConditionApp.instance.getString(R.string.error_log_tag),
                    errorBody.toString()
                )
            }
            return true
        } else {
            return false
        }
    }
}