package it.luccap11.android.weatherconditions

import android.app.Application

/**
 * @author Luca Capitoli
 * @since 23/dec/2020
 */
object OzonoAppl: Application() {
    lateinit var instance: OzonoAppl

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}