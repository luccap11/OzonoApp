package it.luccap11.android.weatherconditions.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.infrastructure.Resource
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.viewmodels.WeatherViewModel
import java.util.*

/**
 * @author Luca Capitoli
 */
class MainFragment : Fragment(), SearchView.OnQueryTextListener,
    Observer<Resource<List<WeatherData>>>, OnItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var weatherResults: RecyclerView
    private lateinit var text: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var listProgressBar: ProgressBar
    private lateinit var citiesResults: RecyclerView
    private lateinit var viewModel: WeatherViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherResults = view.findViewById(R.id.listWeatherData)
        weatherResults.layoutManager = LinearLayoutManager(context)
        text = view.findViewById(R.id.text)
        progressBar = view.findViewById(R.id.progressLoading)

        listProgressBar = view.findViewById(R.id.listLoading)
        citiesResults = view.findViewById(R.id.citiesList)
        citiesResults.layoutManager = LinearLayoutManager(context)

        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.weatherLiveData.observe(viewLifecycleOwner, this)
        viewModel.citiesLiveData.observe(viewLifecycleOwner, Observer { citiesData ->
            when (citiesData) {
                is Resource.Loading -> {
                    listProgressBar.visibility = View.VISIBLE
                    weatherResults.visibility = View.GONE
//                    text.visibility = View.GONE TODO: Have I to add a text view? Or a Empty view?
                }

                is Resource.Error -> {
                    listProgressBar.visibility = View.GONE
                    citiesResults.visibility = View.GONE
                    //text.visibility = View.VISIBLE
                    //text.text = citiesData.message
                }

                is Resource.Success -> {
                    listProgressBar.visibility = View.GONE
                    //text.visibility = View.GONE
                    citiesResults.visibility = View.VISIBLE
                    citiesResults.adapter = CitiesAdapter(citiesData.data!!.take(3), this)
                }
            }
        })
    }

    override fun onChanged(@Nullable weatherData: Resource<List<WeatherData>>) {
        when (weatherData) {
            is Resource.Loading -> {
                progressBar.visibility = View.VISIBLE
                weatherResults.visibility = View.GONE
                text.visibility = View.GONE
            }

            is Resource.Error -> {
                progressBar.visibility = View.GONE
                weatherResults.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.text = weatherData.message
            }

            is Resource.Success -> {
                progressBar.visibility = View.GONE
                text.visibility = View.GONE
                weatherResults.visibility = View.VISIBLE
                weatherResults.adapter = WeatherAdapter(weatherData.data!!)
            }
        }
    }

    override fun onQueryTextSubmit(queryString: String?): Boolean {
        if (!queryString.isNullOrBlank()) {
            viewModel.updateWeatherData(queryString)
        }
        citiesResults.visibility = View.GONE
        return false
    }

    override fun onQueryTextChange(queryString: String?): Boolean {
        if (queryString.isNullOrBlank()) {
            citiesResults.visibility = View.GONE
        } else {
            citiesResults.visibility = View.VISIBLE
            viewModel.updateCityData(queryString.trim().split(" ").joinToString(" "){ it.capitalize(Locale.getDefault())})
        }
        return false
    }

    override fun onItemClick(cityData: CityData) {
        searchView.setQuery(cityData.name, true)
        listProgressBar.visibility = View.GONE
        citiesResults.visibility = View.GONE
    }
}