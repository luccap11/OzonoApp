package it.luccap11.android.ozono.network

import it.luccap11.android.ozono.model.data.WeatherData


interface RemoteWDataSource {
    suspend fun fetchOWeatherMapData(selectedCity: String): List<WeatherData>?
}