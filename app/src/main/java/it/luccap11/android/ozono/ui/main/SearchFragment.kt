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
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.databinding.SearchFragmentBinding
import it.luccap11.android.ozono.model.ApiStatus
import it.luccap11.android.ozono.infrastructure.room.AppDatabase
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.model.data.CityData
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModel
import it.luccap11.android.ozono.model.viewmodels.WeatherViewModelFactory
import it.luccap11.android.ozono.network.AlgoliaCitiesRemoteDataSource
import it.luccap11.android.ozono.network.OWMRemoteDataSource
import it.luccap11.android.ozono.repository.WeatherDataRepository
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.util.PreferencesManager
import java.util.*


/**
 * @author Luca Capitoli
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener,
    Observer<ApiStatus>, OnItemClickListener {
    private val sharedViewModel: WeatherViewModel by activityViewModels { WeatherViewModelFactory(
        WorldCitiesRepository(CitiesDataCache, AppDatabase.getInstance().citiesDao(), AlgoliaCitiesRemoteDataSource, PreferencesManager()),
        WeatherDataRepository(OWMRemoteDataSource)
    ) }
    private val prefs = PreferencesManager()
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private var isCitySelected = false
    private lateinit var fragmentContainerView: FragmentContainerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        binding.citiesList.apply {
            setHasFixedSize(false)
            adapter = CitiesAdapter(this@SearchFragment)
        }
        binding.searchView.apply {
            setIconifiedByDefault(false)
            setOnQueryTextListener(this@SearchFragment)

            findViewById<View>(androidx.appcompat.R.id.search_plate)?.setBackgroundColor(Color.TRANSPARENT)
            val searchIcon = findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
            val searchClose = findViewById<ImageView>(R.id.search_close_btn)
            searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))
            searchClose.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))
        }

//        binding.listWeatherData.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        sharedViewModel.citiesStatus.observe(viewLifecycleOwner, this)

        sharedViewModel.getLastCitySearched()
        sharedViewModel.lastCitySearched.observe(viewLifecycleOwner, { lastCitySearched ->
            if (lastCitySearched != null) {
                onItemClick(lastCitySearched)
            }
        })

        fragmentContainerView = requireActivity().findViewById(R.id.search_fragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChanged(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.citiesDataLoading.visibility = View.VISIBLE
                fragmentContainerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.backgroud_semitransparent_search))
            }

            ApiStatus.ERROR -> {
                binding.citiesDataLoading.visibility = View.GONE
                binding.citiesList.visibility = View.GONE
                fragmentContainerView.setBackgroundColor(Color.TRANSPARENT)
            }

            ApiStatus.SUCCESS -> {
                binding.citiesDataLoading.visibility = View.GONE

                val data = sharedViewModel.citiesData.value
                if (data.isNullOrEmpty()) {
                    binding.citiesList.visibility = View.GONE
                    fragmentContainerView.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    binding.citiesList.visibility = View.VISIBLE
                    fragmentContainerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.backgroud_semitransparent_search))
                }
            }
        }
    }

    override fun onQueryTextSubmit(queryString: String?): Boolean {
        if (!queryString.isNullOrBlank()) {
            sharedViewModel.updateWeatherData(queryString)
            binding.citiesDataLoading.visibility = View.GONE
            binding.citiesList.visibility = View.GONE
            fragmentContainerView.setBackgroundColor(Color.TRANSPARENT)
            binding.searchView.clearFocus()
        }
        return false
    }

    override fun onQueryTextChange(queryString: String?): Boolean {
        if (queryString.isNullOrBlank()) {
            binding.citiesList.visibility = View.GONE
            fragmentContainerView.setBackgroundColor(Color.TRANSPARENT)
        } else {
            if (isCitySelected) {
                isCitySelected = false
            } else {
                sharedViewModel.updateCityData(queryString.trim().split(" ").joinToString(" ") { userQuery ->
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
        isCitySelected = true
        binding.searchView.setQuery(cityData.localeNames.cityNames[0], true)

        savePreference(cityData)
    }

    private fun savePreference(cityData: CityData) {
        prefs.saveLastSearchedCityLatit(cityData.geoloc.lat)
        prefs.saveLastSearchedCityLongit(cityData.geoloc.lng)
    }
}