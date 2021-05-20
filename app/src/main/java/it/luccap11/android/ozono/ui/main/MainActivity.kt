package it.luccap11.android.ozono.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.databinding.MainActivityBinding


/**
 * @author Luca Capitoli
 */
class MainActivity : AppCompatActivity(){
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}