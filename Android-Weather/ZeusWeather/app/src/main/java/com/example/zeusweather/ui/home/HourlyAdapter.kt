package com.example.zeusweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zeusweather.R
import com.example.zeusweather.data.model.Hourly
import com.example.zeusweather.databinding.ItemHourlyBinding
import com.example.zeusweather.util.Constants.IMG_URL
import com.example.zeusweather.util.UnitsConverters
import com.example.zeusweather.util.WeatherUtil.getHourAndMinute

class HourlyAdapter(var hourlyList: ArrayList<Hourly>) :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {
    lateinit var unitsConverters: UnitsConverters

    class HourlyViewHolder(var view: ItemHourlyBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        unitsConverters = UnitsConverters(parent.context)
        return HourlyViewHolder(binding)
    }

    fun changeData(newList: List<Hourly>) {
        hourlyList.clear()
        var list = newList.slice(0..23)
        hourlyList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = hourlyList.size

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val item = hourlyList[position]
        if (position == 0) {
            holder.view.txtHour.text = holder.view.txtHour.context.getString(R.string.current)
        } else
            holder.view.txtHour.text = getHourAndMinute(item.dt.toLong())
        holder.view.txtTemp.text =
            unitsConverters.returnTemperatureUsingUserFormat(
                item.temp.toInt().toString()
            )

        holder.view.txtDesc.text = item.weather[0].description

        Glide.with(holder.view.imgIcon.context)
            .load(IMG_URL + item.weather[0].icon + ".png")
            .placeholder(R.drawable.testimgae)
            .into(holder.view.imgIcon)
    }


}