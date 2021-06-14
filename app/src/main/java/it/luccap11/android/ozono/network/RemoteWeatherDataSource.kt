package it.luccap11.android.ozono.network

import it.luccap11.android.ozono.model.data.WeatherData


interface RemoteWeatherDataSource {
    suspend fun fetchOWeatherMapData(selectedCity: String): WeatherData?
}