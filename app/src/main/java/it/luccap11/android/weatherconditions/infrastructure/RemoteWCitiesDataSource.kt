package it.luccap11.android.weatherconditions.infrastructure

import android.util.Log
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.infrastructure.RetrofitClientInstance.retrofitInstance
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WorldCitiesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLEncoder


/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
object RemoteWCitiesDataSource {//aggiungi test
    private const val THRESHOLD = 0.01

    suspend fun fetchBack4AppData(selectedCity: String): List<CityData> {
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
        val response = call.await()
        return filterDuplicates(response.cities)
    }

    private fun findErrorsInBodyResponse(response: Response<WorldCitiesData>, body: WorldCitiesData?): Boolean {
        return if (body == null) {
            Log.e(OzonoAppl.instance.getString(R.string.error_log_tag), response.message())

            val errorBody = response.errorBody()
            if (errorBody != null) {
                Log.e(OzonoAppl.instance.getString(R.string.error_log_tag), errorBody.toString())
            }
            true
        } else {
            false
        }
    }

    private fun filterDuplicates(remoteCities: List<CityData>): List<CityData> {
        val orderedRemoteCities = remoteCities.sortedByDescending { it.population }
        val uniqueCities = mutableSetOf<CityData>()
        orderedRemoteCities.forEach { cityData ->
            var insertInSet = true
            uniqueCities.forEach { uniqueCity ->
                val latitMaxThreshold = BigDecimal(uniqueCity.location.latitude.toDouble() + THRESHOLD).setScale(2, RoundingMode.HALF_EVEN)
                val latitMinThreshold = BigDecimal(uniqueCity.location.latitude.toDouble() - THRESHOLD).setScale(2, RoundingMode.HALF_EVEN)
                val longitMaxThreshold = BigDecimal(uniqueCity.location.longitude.toDouble() + THRESHOLD).setScale(2, RoundingMode.HALF_EVEN)
                val longitMinThreshold = BigDecimal(uniqueCity.location.longitude.toDouble() - THRESHOLD).setScale(2, RoundingMode.HALF_EVEN)
                val currentLatitRound = BigDecimal(cityData.location.latitude.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                val currentLongitRound = BigDecimal(cityData.location.longitude.toDouble()).setScale(2, RoundingMode.HALF_EVEN)

                if (cityData.name.equals(uniqueCity.name, true) && cityData.country.name.equals(uniqueCity.country.name, true) &&
                    (currentLatitRound in latitMinThreshold..latitMaxThreshold && currentLongitRound in longitMinThreshold..longitMaxThreshold) &&
                    cityData.population <= uniqueCity.population) {
                    insertInSet = false
                }
            }
            if (insertInSet) {
                uniqueCities.add(cityData)
            }
        }

        return uniqueCities.toList()
    }
}