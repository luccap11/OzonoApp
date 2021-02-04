package it.luccap11.android.weatherconditions.model.data

import com.google.gson.annotations.SerializedName
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity

/**
 * @author Luca Capitoli
 * @since 11/jan/2021
 */
data class CityData(@SerializedName("name") val name: String,
                    @SerializedName("country") val country: Country,
                    @SerializedName("population") val population: Int,
                    @SerializedName("location") val location: Location)

data class WorldCitiesData (@SerializedName("results") var cities: Set<CityData> = emptySet())

data class Country(@SerializedName("name") val name: String)

data class Location(@SerializedName("latitude") val latitude: Float,
                    @SerializedName("longitude") val longitude: Float)

class CityDataBuilder {
    fun cityDataBuilder(citiesEntity: List<CityEntity>): List<CityData> {
        val result = mutableListOf<CityData>()
        citiesEntity.forEach { cityEntity ->
            val data = CityData(cityEntity.name, Country(cityEntity.country), cityEntity.population, Location(cityEntity.latitude, cityEntity.longitude))
            result.add(data)
        }
        return result
    }
}