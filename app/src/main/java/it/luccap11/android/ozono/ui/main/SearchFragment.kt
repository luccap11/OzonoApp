package it.luccap11.android.ozono.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.databinding.SearchFragmentBinding
import it.luccap11.android.ozono.repository.OWeatherMapRepository
import it.luccap11.android.ozono.network.RemoteWCitiesDataSource
import it.luccap11.android.ozono.infrastructure.Resource
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.infrastructure.room.AppDatabase
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.model.data.CityData
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModel
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModelFactory
import it.luccap11.android.ozono.utils.PreferencesManager
import java.util.*


/**
 * @author Luca Capitoli
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener,
    Observer<Resource<List<CityData>>>, OnItemClickListener {
    private val sharedViewModel: WeatherViewModel by activityViewModels { WeatherViewModelFactory(
        WorldCitiesRepository(CitiesDataCache, AppDatabase.getInstance().citiesDao(), RemoteWCitiesDataSource()),
        OWeatherMapRepository()
    ) }
    private val prefs = PreferencesManager()
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)?.setBackgroundColor(Color.TRANSPARENT)
        val searchIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        val searchClose = binding.searchView.findViewById<ImageView>(R.id.search_close_btn)
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))
        searchClose.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))

//        binding.listWeatherData.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.sharedViewModel.citiesLiveData.observe(viewLifecycleOwner, this)

        this.sharedViewModel.getLastCitySearched()
        this.sharedViewModel.lastCitySearched.observe(viewLifecycleOwner, { lastCitySearched ->
            if (lastCitySearched != null) {
                binding.searchView.setQuery(lastCitySearched.name, true)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChanged(citiesData: Resource<List<CityData>>) {
        when (citiesData) {
            is Resource.Loading -> {
                binding.citiesDataLoading.visibility = View.VISIBLE
//                    binding.listWeatherData.visibility = View.GONE
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
    }

    override fun onQueryTextSubmit(queryString: String?): Boolean {
        if (!queryString.isNullOrBlank()) {
            this.sharedViewModel.updateWeatherData(queryString)
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
                this.sharedViewModel.updateCityData(queryString.trim().split(" ").joinToString(" ") { userQuery ->
                    userQuery.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()
                        ) else it.toString()
                    }
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