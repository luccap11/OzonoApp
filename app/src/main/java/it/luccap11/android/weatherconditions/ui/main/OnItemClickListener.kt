package it.luccap11.android.weatherconditions.ui.main

import it.luccap11.android.weatherconditions.model.data.CityData

/**
 * Interface for RecyclerView item click.
 * @author Luca Capitoli
 * @since 21/jan/2021
 */
interface OnItemClickListener {
    fun onItemClick(cityData: CityData)
}