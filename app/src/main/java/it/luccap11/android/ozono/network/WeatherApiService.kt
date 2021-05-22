package it.luccap11.android.ozono.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import it.luccap11.android.ozono.model.data.WeatherData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaces that define the possible HTTP operations
 * @author Luca Capitoli
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

private const val OWM_API_KEY = "7d85604d75067f7a0da53ac8f70c5364"

interface WeatherApiService {

    @GET("forecast?APPID=${OWM_API_KEY}&units=metric")
    suspend fun fetchRemoteWeatherData(@Query("q") selectedCity: String): WeatherData
}