package com.example.zeusweather.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zeusweather.R
import com.example.zeusweather.data.model.Daily
import com.example.zeusweather.databinding.ItemDailyBinding
import com.example.zeusweather.util.Constants.IMG_URL
import com.example.zeusweather.util.UnitsConverters
import java.util.*
import java.util.Calendar.*

class DailyAdapter(var dailyList: ArrayList<Daily>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    class DailyViewHolder(var view: ItemDailyBinding) : RecyclerView.ViewHolder(view.root)

    lateinit var unitsConverters: UnitsConverters
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        unitsConverters = UnitsConverters(parent.context)
        return DailyViewHolder(binding)
    }

    fun changeData(newList: List<Daily>) {
        dailyList.clear()
        dailyList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val item = dailyList[position]
        val calendar: Calendar = getInstance()
        calendar.setTimeInMillis(dailyList[position].dt.toLong() * 1000)

        holder.view.dailyDayTextivew.text = localizingDays(
            calendar.getDisplayName(DAY_OF_WEEK, LONG, Locale.getDefault()),
            holder.view.dailyDayTextivew.context
        )
        holder.view.dailyDescriptionTextview.text = item.weather[0].description
        holder.view.dailyMaxTextview.text =
            unitsConverters.returnTemperatureUsingUserFormat(
                item.temp.max.toInt().toString()
            )

        holder.view.dailyMinTextview.text = unitsConverters.returnTemperatureUsingUserFormat(
            item.temp.min.toInt().toString()
        )
        Glide.with(holder.view.dailyImageImageView.context)
            .load(IMG_URL + item.weather[0].icon + "@2x.png")
            .placeholder(R.drawable.testimgae)
            .into(holder.view.dailyImageImageView)

    }

    override fun getItemCount(): Int = dailyList.size

    private fun localizingDays(day: String, context: Context): String {

        return when (day.trim()) {
            "Saturday" -> context.getString(R.string.saturday)
            "Sunday" -> context.getString(R.string.sunday)
            "Monday" -> context.getString(R.string.monday)
            "Tuesday" -> context.getString(R.string.tuesday)
            "Wednesday" -> context.getString(R.string.wednesday)
            "Friday" -> context.getString(R.string.friday)
            "Thursday" -> context.getString(R.string.thursday)
            else -> day
        }

    }
}