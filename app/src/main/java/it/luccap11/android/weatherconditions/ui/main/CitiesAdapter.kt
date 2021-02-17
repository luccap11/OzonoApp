package it.luccap11.android.weatherconditions.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.model.data.CityData

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class CitiesAdapter(private val dataSet: List<CityData>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {
    private lateinit var context : Context

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityDescr: TextView = view.findViewById(R.id.cityDescr)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.city_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.cityDescr.text = String.format("%s, %s, %s", dataSet[position].name, dataSet[position].adminCode, dataSet[position].country.name)
        viewHolder.itemView.setOnClickListener {
            listener.onItemClick(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

}