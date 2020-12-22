package it.luccap11.android.weatherconditions.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.ui.main.MainFragment


/**
 * @author Luca Capitoli
 */
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}