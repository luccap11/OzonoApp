package it.luccap11.android.ozono.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import it.luccap11.android.ozono.model.data.AlgoliaModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaces that define the possible HTTP operations
 * @author Luca Capitoli
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private const val BASE_URL = "https://places-dsn.algolia.net/1/places/"

val retrofitCities: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface CitiesApiService {

    @POST("query")
    suspend fun fetchRemoteCitiesByName(@Body query: BodyParams): AlgoliaModel
}

data class BodyParams(
    val query: String,
    val type: String = "city",
    val hitsPerPage: Int = 10
)