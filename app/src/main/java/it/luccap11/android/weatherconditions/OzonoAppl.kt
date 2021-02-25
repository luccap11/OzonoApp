package it.luccap11.android.weatherconditions

import android.app.Application

/**
 * @author Luca Capitoli
 * @since 23/dec/2020
 */
class OzonoAppl: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: OzonoAppl
            private set
    }
}