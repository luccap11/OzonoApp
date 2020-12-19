package it.luccap11.android.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

/**
 * @author Luca Capitoli
 */
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var searchView : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bestCities = applicationContext.resources.getStringArray(R.array.bestCities)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bestCities)
        val cities = findViewById<ListView>(R.id.citiesListView)
        cities.adapter = adapter
        cities.onItemClickListener = this

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryString: String?): Boolean {
                if (bestCities.contains(queryString)) {
                    adapter.filter.filter(queryString)
                }
                cities.visibility = View.GONE

                return false
            }
            override fun onQueryTextChange(queryString: String?): Boolean {
                if (queryString.isNullOrBlank())
                    cities.visibility = View.GONE
                else {
                    cities.visibility = View.VISIBLE
                    adapter.filter.filter(queryString)
                }
                return false
            }
        })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        view as TextView
        searchView.setQuery(view.text, true)
    }
}