package it.luccap11.android.ozono.network

import android.util.Log
import it.luccap11.android.ozono.model.data.CityData
import it.luccap11.android.ozono.util.AppUtils.TAG_LOG

/**
 * @author Luca Capitoli
 * @since 30/dec/2020
 */
object AlgoliaCitiesRemoteDataSource : RemoteCitiesDataSource {
    private val retrofitService: CitiesApiService by lazy {
        retrofitCities.create(CitiesApiService::class.java)
    }

    override suspend fun fetchAlgoliaData(selectedCity: String): List<CityData>? {
        try {
            return retrofitService.fetchRemoteCitiesByName(BodyParams(selectedCity)).list
        } catch (e: Exception) {
            Log.e(TAG_LOG, e.stackTraceToString(), e)
        }
        return null
    }
}