package it.luccap11.android.weatherconditions.model.data

import com.google.gson.annotations.SerializedName

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