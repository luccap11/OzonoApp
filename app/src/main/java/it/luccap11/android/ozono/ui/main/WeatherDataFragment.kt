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
import it.luccap11.android.ozono.infrastructure.ApiStatus
import it.luccap11.android.ozono.repository.WeatherDataRepository
import it.luccap11.android.ozono.network.RemoteWCitiesDataSource
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.infrastructure.room.AppDatabase
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.model.data.ListData
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModel
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModelFactory
import it.luccap11.android.ozono.network.OWMRemoteDataSource
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * @author Luca Capitoli
 */
class WeatherDataFragment : Fragment(), Observer<ApiStatus> {
    private val sharedViewModel: WeatherViewModel by activityViewModels { WeatherViewModelFactory(
        WorldCitiesRepository(CitiesDataCache, AppDatabase.getInstance().citiesDao(), RemoteWCitiesDataSource()),
        WeatherDataRepository(OWMRemoteDataSource)
    ) }
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
                binding.resultMessage.visibility = View.VISIBLE
                binding.resultMessage.text = resources.getString(R.string.error_label)
            }

            ApiStatus.SUCCESS -> {
                binding.weatherDataLoading.visibility = View.GONE
                binding.emptyWeatherImage.visibility = View.GONE
                binding.listWeatherData.visibility = View.VISIBLE

                val data = sharedViewModel.weatherData.value!!.list
                binding.listWeatherData.adapter = WeatherAdapter(temp_filterData(data))

                if (data.isEmpty()) {
                    binding.resultMessage.visibility = View.VISIBLE
                    binding.resultMessage.text = resources.getText(R.string.no_data_label)
                } else {
                    binding.resultMessage.visibility = View.GONE
                }
            }
        }
    }

    private fun temp_filterData(original: List<ListData>): List<ListData> {
        val days = mutableSetOf<String>()
        val result = mutableListOf<ListData>()
        original.forEach {
            val day = dateFormatter(it.timeInSecs)
            if (!days.contains(day)) {
                result.add(it)
                days.add(day)
            }
        }
        return result
    }

    private fun dateFormatter(timeInMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE")
        val instant = Instant.ofEpochMilli(timeInMillis * 1000)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }
}