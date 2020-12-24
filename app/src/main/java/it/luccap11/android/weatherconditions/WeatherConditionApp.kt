package it.luccap11.android.weatherconditions

import android.app.Application

class WeatherConditionApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: WeatherConditionApp
            private set
    }
}