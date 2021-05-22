package it.luccap11.android.ozono.model.data

import com.squareup.moshi.Json

/**
 * @author Luca Capitoli
 */
data class WeatherData(
    @Json(name = "list") val list: List<ListData>,
    @Json(name = "city") val city: City
)

data class City (
    @Json(name = "name") val name: String
)

data class ListData (
    @Json(name = "dt") val timeInMillis: Long,
    @Json(name = "dt_txt") val date: String,
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "main") val main: Main
        )

data class Weather (
    @Json(name = "main") val descr: String,
    @Json(name = "icon") val icon: String
//    @Json(name = "description") val descr: String
)

data class Main (
    @Json(name = "temp") val temp: Float
)