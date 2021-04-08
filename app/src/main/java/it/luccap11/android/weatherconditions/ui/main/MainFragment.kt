package it.luccap11.android.weatherconditions.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.infrastructure.OWeatherMapRepository
import it.luccap11.android.weatherconditions.infrastructure.Resource
import it.luccap11.android.weatherconditions.infrastructure.WorldCitiesRepository
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.viewmodels.WeatherViewModel
import it.luccap11.android.weatherconditions.model.viewmodels.WeatherViewModelFactory
import it.luccap11.android.weatherconditions.utils.PreferencesManager
import java.util.*


/**
 * @author Luca Capitoli
 */
class MainFragment : Fragment(), SearchView.OnQueryTextListener,
    Observer<Resource<List<WeatherData>>>, OnItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var weatherResults: RecyclerView
    private lateinit var emptyWeatherData: ImageView
    private lateinit var weatherProgressBar: ProgressBar
    private lateinit var citiesProgressBar: ProgressBar
    private lateinit var citiesResults: RecyclerView
    private val viewModel: WeatherViewModel by viewModels { WeatherViewModelFactory(
        WorldCitiesRepository(),
        OWeatherMapRepository()
    ) }
    private val prefs = PreferencesManager()

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
        emptyWeatherData = view.findViewById(R.id.emptyWeatherImage)
        weatherProgressBar = view.findViewById(R.id.weatherDataLoading)

        citiesProgressBar = view.findViewById(R.id.citiesDataLoading)
        citiesResults = view.findViewById(R.id.citiesList)
        citiesResults.layoutManager = LinearLayoutManager(context)

        searchView = view.findViewById(R.id.searchView)
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(this)
        searchView.isQueryRefinementEnabled = true
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.weatherLiveData.observe(viewLifecycleOwner, this)
        viewModel.citiesLiveData.observe(viewLifecycleOwner, { citiesData ->
            when (citiesData) {
                is Resource.Loading -> {
                    citiesProgressBar.visibility = View.VISIBLE
                    weatherResults.visibility = View.GONE
                }

                is Resource.Error -> {
                    citiesProgressBar.visibility = View.GONE
                    citiesResults.visibility = View.GONE
                }

                is Resource.Success -> {
                    citiesProgressBar.visibility = View.GONE
                    citiesResults.visibility = View.VISIBLE
                    citiesResults.adapter = CitiesAdapter(
                        citiesData.data!!.take(
                            resources.getInteger(
                                R.integer.num_of_cities_result
                            )
                        ), this
                    )
                }
            }
        })

        viewModel.getLastCitySearched()
        viewModel.lastCitySearched.observe(viewLifecycleOwner, { lastCitySearched ->
            if (lastCitySearched != null) {
                searchView.setQuery(lastCitySearched.name, true)
            }
        })
    }

    override fun onChanged(@Nullable weatherData: Resource<List<WeatherData>>) {
        when (weatherData) {
            is Resource.Loading -> {
                weatherProgressBar.visibility = View.VISIBLE
                weatherResults.visibility = View.GONE
                emptyWeatherData.visibility = View.GONE
            }

            is Resource.Error -> {
                weatherProgressBar.visibility = View.GONE
                weatherResults.visibility = View.GONE
                emptyWeatherData.visibility = View.VISIBLE
            }

            is Resource.Success -> {
                weatherProgressBar.visibility = View.GONE
                emptyWeatherData.visibility = View.GONE
                weatherResults.visibility = View.VISIBLE
                weatherResults.adapter = WeatherAdapter(weatherData.data!!)
            }
        }
    }

    override fun onQueryTextSubmit(queryString: String?): Boolean {
        if (!queryString.isNullOrBlank()) {
            viewModel.updateWeatherData(queryString)
        }
        return false
    }

    override fun onQueryTextChange(queryString: String?): Boolean {
        if (queryString.isNullOrBlank()) {
            citiesResults.visibility = View.GONE
        } else {
            if (searchView.isIconified) {
                searchView.isIconified = false
            } else {
                citiesResults.visibility = View.VISIBLE
                viewModel.updateCityData(queryString.trim().split(" ").joinToString(" ") {
                    it.capitalize(
                        Locale.getDefault()
                    )
                })
            }
        }
        return false
    }

    override fun onItemClick(cityData: CityData) {
        searchView.setQuery(cityData.name, true)
        citiesProgressBar.visibility = View.GONE
        citiesResults.visibility = View.GONE
        savePreference(cityData)
    }

    private fun savePreference(cityData: CityData) {
        prefs.saveLastSearchedCityLatit(cityData.location.latitude)
        prefs.saveLastSearchedCityLongit(cityData.location.longitude)
    }
}