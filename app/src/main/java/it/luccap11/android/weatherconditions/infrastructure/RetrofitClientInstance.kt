package it.luccap11.android.weatherconditions.infrastructure

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://parseapi.back4app.com/classes/"
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}