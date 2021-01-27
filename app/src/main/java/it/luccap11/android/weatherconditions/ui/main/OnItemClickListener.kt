package it.luccap11.android.weatherconditions.ui.main

import androidx.annotation.NonNull
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WeatherData

/**
 * Interface for RecyclerView item click.
 * @author Luca Capitoli
 * @since 21/jan/2021
 */
interface OnItemClickListener {
    fun onItemClick(@NonNull cityData: CityData)
}