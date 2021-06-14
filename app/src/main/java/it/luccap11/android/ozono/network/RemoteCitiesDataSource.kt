package it.luccap11.android.ozono.network

import it.luccap11.android.ozono.model.data.CityData


interface RemoteCitiesDataSource {
    suspend fun fetchAlgoliaData(selectedCity: String): List<CityData>?
}