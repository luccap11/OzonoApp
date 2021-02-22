package it.luccap11.android.weatherconditions

import android.app.Application

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