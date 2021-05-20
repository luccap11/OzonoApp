package it.luccap11.android.ozono.network

import it.luccap11.android.ozono.model.data.WorldCitiesData
import retrofit2.Call
import retrofit2.http.*

/**
 * Interfaces that define the possible HTTP operations
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
interface WorldCitiesWS {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @Headers("X-Parse-Application-Id: Mg9TaDgz2AfOLyS7pH5aI6Nqaqqsw5zuZq1j7TqV",
        "X-Parse-REST-API-Key: BgTfYWFcm26lRVn6ccCvyrZzZdZwhmvv0OzycDmv"
    )
    @GET("Continentscountriescities_City?count=1&limit=9&order=name,country&include=country&keys=name,country,country.name,country.continent,population,location,cityId,adminCode")
    fun getCities(@Query("where", encoded = true) where: String): Call<WorldCitiesData>
}