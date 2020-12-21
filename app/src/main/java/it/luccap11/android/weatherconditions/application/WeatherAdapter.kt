package it.luccap11.android.weatherconditions.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.model.WeatherData

/**
 * @author Luca Capitoli
 */
class WeatherAdapter(private val dataSet: List<WeatherData>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.day)
        val descr: TextView = view.findViewById(R.id.descr)
        val temp: TextView = view.findViewById(R.id.temperature)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.weather_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.date.text = dataSet[position].date.subSequence(0, 10)
        viewHolder.descr.text = dataSet[position].descr
        viewHolder.temp.text = String.format("%d °F", dataSet[position].temp.toInt())
    }

    override fun getItemCount() = dataSet.size

}