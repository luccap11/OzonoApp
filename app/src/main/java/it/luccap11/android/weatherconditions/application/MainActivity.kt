package it.luccap11.android.weatherconditions.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.model.Resource
import it.luccap11.android.weatherconditions.model.WeatherData
import it.luccap11.android.weatherconditions.model.WeatherViewModel


/**
 * @author Luca Capitoli
 */
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener,
    Observer<Resource<List<WeatherData>>> {
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: WeatherViewModel by viewModels()
        viewModel.liveData.observe(this, this)

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
                viewModel.getData(queryString!!)

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

    override fun onChanged(weatherData: Resource<List<WeatherData>>) {
        val results = findViewById<RecyclerView>(R.id.listWeatherData)
        results.layoutManager = LinearLayoutManager(this)

        // initialize an instance of divider item decoration
        DividerItemDecoration(
            this, LinearLayoutManager.HORIZONTAL
        ).apply {
            // add divider item decoration to recycler view
            // this will show divider line between items
            results.addItemDecoration(this)
        }

//        results.addItemDecoration(
//            DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL)
//        )
        val progressBar = findViewById<ProgressBar>(R.id.progressLoading)
        if (weatherData.data == null && weatherData.message.isNullOrBlank()) {
            progressBar.visibility = View.VISIBLE
            results.visibility = View.GONE
        } else if (weatherData.data == null) {
            // TODO
            progressBar.visibility = View.GONE
            results.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            results.visibility = View.VISIBLE
            val weatherAdapter = WeatherAdapter(weatherData.data)
            results.adapter = weatherAdapter
            //results.text = weatherData.data[0].location + " - " + weatherData.data[0].descr + " - " + weatherData.data[0].temp + " - " + weatherData.data[0].icon
        }
    }
}