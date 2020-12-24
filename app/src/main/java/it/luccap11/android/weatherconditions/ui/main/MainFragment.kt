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
import it.luccap11.android.weatherconditions.model.Resource
import it.luccap11.android.weatherconditions.model.WeatherViewModel
import it.luccap11.android.weatherconditions.model.data.WeatherData

/**
 * @author Luca Capitoli
 */
class MainFragment : Fragment(), AdapterView.OnItemClickListener, SearchView.OnQueryTextListener,
    Observer<Resource<List<WeatherData>>> {
    private lateinit var searchView: SearchView
    private lateinit var results: RecyclerView
    private lateinit var text: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var cities: ListView
    private lateinit var viewModel: WeatherViewModel
    private lateinit var bestCities: Array<String>
    private lateinit var adapter: ArrayAdapter<String>

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
        results = view.findViewById(R.id.listWeatherData)
        results.layoutManager = LinearLayoutManager(context)
        text = view.findViewById(R.id.text)
        progressBar = view.findViewById(R.id.progressLoading)

        bestCities = requireContext().resources.getStringArray(R.array.bestCities)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, bestCities)
        cities = view.findViewById(R.id.citiesListView)
        cities.adapter = adapter
        cities.onItemClickListener = this

        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        view as TextView
        searchView.setQuery(view.text, true)
    }

    override fun onChanged(@Nullable weatherData: Resource<List<WeatherData>>) {
        when (weatherData) {
            is Resource.Loading -> {
                progressBar.visibility = View.VISIBLE
                results.visibility = View.GONE
                text.visibility = View.GONE
            }

            is Resource.Error -> {
                progressBar.visibility = View.GONE
                results.visibility = View.GONE
                text.visibility = View.VISIBLE
                text.text = weatherData.message
            }

            is Resource.Success -> {
                progressBar.visibility = View.GONE
                text.visibility = View.GONE
                results.visibility = View.VISIBLE
                results.adapter = WeatherAdapter(weatherData.data!!)
            }
        }
    }

    override fun onQueryTextSubmit(queryString: String?): Boolean {
        if (!queryString.isNullOrBlank()) {
            if (bestCities.contains(queryString)) {
                adapter.filter.filter(queryString)
            }
            viewModel.updateWeatherData(queryString)
        }
        cities.visibility = View.GONE
        return false
    }

    override fun onQueryTextChange(queryString: String?): Boolean {
        if (queryString.isNullOrBlank()) {
            cities.visibility = View.GONE
        } else {
            cities.visibility = View.VISIBLE
            adapter.filter.filter(queryString)
        }
        return false
    }
}