package it.luccap11.android.ozono.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.luccap11.android.ozono.R


/**
 * @author Luca Capitoli
 */
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }
}