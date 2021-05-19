package it.luccap11.android.ozono.utils

import android.content.Context
import it.luccap11.android.ozono.OzonoAppl
import it.luccap11.android.ozono.R

/**
 * @author Luca Capitoli
 * @since 24/feb/2021
 */
class PreferencesManager {
    private val context = OzonoAppl.instance
    private val sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    fun saveLastSearchedCityLatit(latit: Float) {
        sharedPref.edit().putFloat(context.getString(R.string.preference_last_searched_city_latit), latit).apply()
    }

    fun saveLastSearchedCityLongit(longit: Float) {
        sharedPref.edit().putFloat(context.getString(R.string.preference_last_searched_city_longit), longit).apply()
    }

    fun getLastSearchedCityLatit(): Float {
        return sharedPref.getFloat(context.getString(R.string.preference_last_searched_city_latit),
            AppUtils.NOT_SET.toFloat()
        )
    }

    fun getLastSearchedCityLongit(): Float {
        return sharedPref.getFloat(context.getString(R.string.preference_last_searched_city_longit),
            AppUtils.NOT_SET.toFloat()
        )
    }

}