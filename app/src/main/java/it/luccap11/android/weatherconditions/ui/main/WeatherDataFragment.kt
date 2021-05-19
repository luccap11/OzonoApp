package it.luccap11.android.weatherconditions.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import it.luccap11.android.weatherconditions.databinding.WeatherDataFragmentBinding
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


/**
 * @author Luca Capitoli
 */
class WeatherDataFragment : Fragment(), Observer<Resource<List<WeatherData>>> {
    private val viewModel: WeatherViewModel by viewModels { WeatherViewModelFactory(
        WorldCitiesRepository(CitiesDataCache, AppDatabase.getInstance().citiesDao(), RemoteWCitiesDataSource()),
        OWeatherMapRepository()
    ) }
    private val prefs = PreferencesManager()
    private var _binding: WeatherDataFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherDataFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.weatherLiveData.observe(viewLifecycleOwner, this)
//        viewModel.citiesLiveData.observe(viewLifecycleOwner, { citiesData ->
//            when (citiesData) {
//                is Resource.Loading -> {
////                    binding.citiesDataLoading.visibility = View.VISIBLE
//                    binding.listWeatherData.visibility = View.GONE
//                }
//            }
//        })
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
}