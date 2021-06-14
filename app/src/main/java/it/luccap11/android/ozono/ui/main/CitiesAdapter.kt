package it.luccap11.android.ozono.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.ozono.databinding.CityRowItemBinding
import it.luccap11.android.ozono.model.data.CityData

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class CitiesAdapter(private val listener: OnItemClickListener) : ListAdapter<CityData, CitiesAdapter.ViewHolder>(
    DiffCallback
) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(private var binding: CityRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityData: CityData) {
            binding.city = cityData
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(CityRowItemBinding.inflate(inflater, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val cityData = getItem(position)
        viewHolder.bind(cityData)

        viewHolder.itemView.setOnClickListener {
            listener.onItemClick(cityData)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CityData>() {

        override fun areItemsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem.geoloc == newItem.geoloc
        }

        override fun areContentsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem.geoloc == newItem.geoloc
        }
    }

}