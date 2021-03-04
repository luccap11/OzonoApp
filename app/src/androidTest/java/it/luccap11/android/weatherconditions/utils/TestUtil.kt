package it.luccap11.android.weatherconditions.utils

import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity

/**
 * @author Luca Capitoli
 * @since 04/mar/2021
 */
object TestUtil {

    fun createCities(number: Int, vararg cityName: String): Array<CityEntity> {
        val result = mutableListOf<CityEntity>()
        for (i in 0 until number) {
            result.add(CityEntity("", "", i, "b", i.toFloat(), i.toFloat(), System.currentTimeMillis()))
        }
        return result.toTypedArray()
    }
}