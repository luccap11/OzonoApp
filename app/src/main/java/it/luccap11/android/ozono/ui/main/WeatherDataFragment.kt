package it.luccap11.android.ozono.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.databinding.WeatherDataFragmentBinding
import it.luccap11.android.ozono.model.ApiStatus
import it.luccap11.android.ozono.repository.WeatherDataRepository
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.infrastructure.room.AppDatabase
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModel
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModelFactory
import it.luccap11.android.ozono.network.AlgoliaCitiesRemoteDataSource
import it.luccap11.android.ozono.network.OWMRemoteDataSource
import it.luccap11.android.ozono.util.PreferencesManager


/**
 * @author Luca Capitoli
 */
class WeatherDataFragment : Fragment(), Observer<ApiStatus> {
    private val sharedViewModel: WeatherViewModel by activityViewModels { WeatherViewModelFactory(
        WorldCitiesRepository(CitiesDataCache, AppDatabase.getInstance().citiesDao(), AlgoliaCitiesRemoteDataSource, PreferencesManager()),
        WeatherDataRepository(OWMRemoteDataSource)
    ) }
    private var _binding: WeatherDataFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherDataFragmentBinding.inflate(inflater, container, false)

        binding.listWeatherData.apply {
            setHasFixedSize(true)
            adapter = WeatherAdapter()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        sharedViewModel.weatherStatus.observe(viewLifecycleOwner, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChanged(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.weatherDataLoading.visibility = View.VISIBLE
                binding.listWeatherData.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.GONE
            }

            ApiStatus.ERROR -> {
                binding.weatherDataLoading.visibility = View.GONE
                binding.listWeatherData.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.VISIBLE
                binding.emptyWeatherImage.setImageResource(R.drawable.ic_eyes_down)
                binding.resultMessage.visibility = View.VISIBLE
                binding.resultMessage.text = resources.getString(R.string.error_label)
            }

            ApiStatus.SUCCESS -> {
                binding.weatherDataLoading.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.GONE
                binding.listWeatherData.visibility = View.VISIBLE

                sharedViewModel.weatherData.value?.let {
                    if (it.isEmpty()) {
                        binding.resultMessage.visibility = View.VISIBLE
                        binding.resultMessage.text = resources.getText(R.string.no_data_label)
                    } else {
                        binding.resultMessage.visibility = View.GONE
                    }
                }
            }
        }
    }
}