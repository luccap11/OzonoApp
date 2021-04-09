package it.luccap11.android.weatherconditions.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.databinding.MainFragmentBinding
import it.luccap11.android.weatherconditions.infrastructure.OWeatherMapRepository
import it.luccap11.android.weatherconditions.infrastructure.RemoteWCitiesDataSource
import it.luccap11.android.weatherconditions.infrastructure.Resource
import it.luccap11.android.weatherconditions.infrastructure.WorldCitiesRepository
import it.luccap11.android.weatherconditions.infrastructure.room.AppDatabase
import it.luccap11.android.weatherconditions.model.data.CitiesDataCache
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
    private val viewModel: WeatherViewModel by viewModels { WeatherViewModelFactory(
        WorldCitiesRepository(CitiesDataCache, AppDatabase.getInstance().citiesDao(), RemoteWCitiesDataSource()),
        OWeatherMapRepository()
    ) }
    private val prefs = PreferencesManager()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.listWeatherData.layoutManager = LinearLayoutManager(context)
        binding.citiesList.layoutManager = LinearLayoutManager(context)

        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)?.setBackgroundColor(Color.TRANSPARENT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.weatherLiveData.observe(viewLifecycleOwner, this)
        viewModel.citiesLiveData.observe(viewLifecycleOwner, { citiesData ->
            when (citiesData) {
                is Resource.Loading -> {
                    binding.citiesDataLoading.visibility = View.VISIBLE
                    binding.listWeatherData.visibility = View.GONE
                }

                is Resource.Error -> {
                    binding.citiesDataLoading.visibility = View.GONE
                    binding.citiesList.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.citiesDataLoading.visibility = View.GONE
                    binding.citiesList.visibility = View.VISIBLE
                    binding.citiesList.adapter = CitiesAdapter(
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
                binding.searchView.setQuery(lastCitySearched.name, true)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChanged(weatherData: Resource<List<WeatherData>>) {
        when (weatherData) {
            is Resource.Loading -> {
                binding.weatherDataLoading.visibility = View.VISIBLE
                binding.listWeatherData.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.GONE
            }

            is Resource.Error -> {
                binding.weatherDataLoading.visibility = View.GONE
                binding.listWeatherData.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.VISIBLE
            }

            is Resource.Success -> {
                binding.weatherDataLoading.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.GONE
                binding.listWeatherData.visibility = View.VISIBLE
                binding.listWeatherData.adapter = WeatherAdapter(weatherData.data!!)
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
            binding.citiesList.visibility = View.GONE
        } else {
            if (binding.searchView.isIconified) {
                binding.searchView.isIconified = false
            } else {
                binding.citiesList.visibility = View.VISIBLE
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
        binding.searchView.isIconified = true //text is cleared
        binding.searchView.isIconified = true //keyboard and search view get closed
        binding.searchView.setQuery(cityData.name, true)
        binding.citiesDataLoading.visibility = View.GONE
        binding.citiesList.visibility = View.GONE
        savePreference(cityData)
    }

    private fun savePreference(cityData: CityData) {
        prefs.saveLastSearchedCityLatit(cityData.location.latitude)
        prefs.saveLastSearchedCityLongit(cityData.location.longitude)
    }
}