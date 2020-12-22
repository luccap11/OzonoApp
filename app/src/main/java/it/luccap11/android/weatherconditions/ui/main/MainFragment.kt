package it.luccap11.android.weatherconditions.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
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
class MainFragment : Fragment(), AdapterView.OnItemClickListener,
    Observer<Resource<List<WeatherData>>> {
    private lateinit var searchView: SearchView
    private lateinit var results: RecyclerView
    private lateinit var text: TextView
    private lateinit var progressBar: ProgressBar
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        results = view.findViewById(R.id.listWeatherData)
        text = view.findViewById(R.id.text)
        progressBar = view.findViewById(R.id.progressLoading)

        val bestCities = requireContext().resources.getStringArray(R.array.bestCities)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, bestCities)
        val cities = view.findViewById<ListView>(R.id.citiesListView)
        cities.adapter = adapter
        cities.onItemClickListener = this

        searchView = view.findViewById(R.id.searchView)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        view as TextView
        searchView.setQuery(view.text, true)
    }

    override fun onChanged(weatherData: Resource<List<WeatherData>>) {
        results.layoutManager = LinearLayoutManager(context)
        if (weatherData.data == null && weatherData.message.isNullOrBlank()) {
            progressBar.visibility = View.VISIBLE
            results.visibility = View.GONE
            text.visibility = View.GONE
        } else if (weatherData.data == null) {
            // TODO
            progressBar.visibility = View.GONE
            results.visibility = View.GONE
            text.visibility = View.VISIBLE
            text.text = weatherData.message
        } else {
            progressBar.visibility = View.GONE
            text.visibility = View.GONE
            results.visibility = View.VISIBLE
            val weatherAdapter = WeatherAdapter(weatherData.data)
            results.adapter = weatherAdapter
        }
    }

}