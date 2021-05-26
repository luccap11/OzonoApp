package it.luccap11.android.ozono.model.data

import com.squareup.moshi.Json
import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity

data class NewCityData(
    @Json(name = "hits") val list: List<Hits>
)

data class Hits(
    @Json(name = "country") val country: CountryC,
    @Json(name = "administrative") val administrative: List<String>,
    @Json(name = "_geoloc") val geoloc: Geoloc,
    @Json(name = "locale_names") val localeNames: LocaleNames

)

data class CountryC(
    @Json(name = "default") val name: String
)

data class Geoloc(
    @Json(name = "lat") val lat: Float,
    @Json(name = "lng") val lng: Float
)

data class LocaleNames(
    @Json(name = "default") val default: List<String>
)

class CityDataBuilder {
    fun cityDataBuilder(entities: List<CityEntity>): List<Hits> {
        var result = listOf<Hits>()
        entities.forEach {
            result = result.plusElement(Hits(CountryC(it.country), listOf(it.adminCode), Geoloc(it.latitude, it.longitude),
                LocaleNames(listOf(it.name))))
        }
        return result
    }
}