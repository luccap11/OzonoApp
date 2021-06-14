package it.luccap11.android.ozono.model.data

import com.squareup.moshi.Json
import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity

data class AlgoliaModel(
    @Json(name = "hits") val list: List<CityData>
)

data class CityData(
    @Json(name = "country") val country: Country,
    @Json(name = "administrative") val region: List<String>,
    @Json(name = "_geoloc") val geoloc: Geoloc,
    @Json(name = "locale_names") val localeNames: LocaleNames

)

data class Country(
    @Json(name = "default") val name: String
)

data class Geoloc(
    @Json(name = "lat") val lat: Float,
    @Json(name = "lng") val lng: Float
)

data class LocaleNames(
    @Json(name = "default") val cityNames: List<String>
)

class CityDataBuilder {
    fun cityDataBuilder(entities: List<CityEntity>): List<CityData> {
        var result = listOf<CityData>()
        entities.forEach {
            result = result.plusElement(CityData(Country(it.country), listOf(it.adminCode), Geoloc(it.latitude, it.longitude),
                LocaleNames(listOf(it.name))))
        }
        return result
    }
}