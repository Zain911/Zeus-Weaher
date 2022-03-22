package com.example.zeusweather.ui.favourite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import com.example.zeusweather.R
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.databinding.FragmentHomeBinding
import com.example.zeusweather.ui.home.DailyAdapter
import com.example.zeusweather.ui.home.HourlyAdapter
import com.example.zeusweather.util.Constants
import com.example.zeusweather.util.UnitsConverters
import com.example.zeusweather.util.WeatherUtil
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteDetailsFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: FavouriteViewModel by viewModels()
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter

    private val binding get() = _binding!!
    lateinit var weatherResponse: WeatherResponse
    lateinit var receivedWeatherData: WeatherResponse
    lateinit var sharedPreferencesFactory: SharedPreferencesFactory
    lateinit var lang :String
    lateinit var unitsConverters: UnitsConverters
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        unitsConverters = UnitsConverters(requireContext())
        lang = WeatherUtil.getCurrentLanguage(requireContext())
         viewModel._detailsSelectedItem.observe(viewLifecycleOwner,
            object : Observer<WeatherResponse> {
                override fun onChanged(t: WeatherResponse?) {
                    Log.d("FragmentFavourite", "onChanged: ${t.toString()} ")
                }
            })
        hourlyAdapter = HourlyAdapter(arrayListOf())
        dailyAdapter = DailyAdapter(arrayListOf())
        initRecyclers()
        val str = arguments?.getString("weatherString")
        receivedWeatherData = Gson().fromJson(str, WeatherResponse::class.java)
        if (WeatherUtil.checkForInternet(requireContext())) {
            lifecycle.coroutineScope.launch {
                weatherResponse =
                    viewModel.makeRequest(
                        receivedWeatherData.lat,
                        receivedWeatherData.lon,
                        receivedWeatherData.timezone,
                        lang
                    ).body()!!
                updateUI(weatherResponse)
                binding.animationView.visibility = View.GONE
            }
        } else
            updateUI(receivedWeatherData)



        viewModel.detailsSelectedItem.observe(viewLifecycleOwner) {
            Log.d("FragmentFavourite", "onChanged: ${it.toString()} ")
        }


        return root
    }

    private fun initRecyclers() {
        binding.rv24Hour.apply {
            adapter = hourlyAdapter
        }
        binding.sevenDayForecastRecycleview.apply {
            adapter = dailyAdapter
        }
    }

    private fun updateUI(weatherResponse: WeatherResponse) {
        //todo handle degree symbol and converter temp
        weatherResponse.daily[0].let {
            "${getString(R.string.max)}${ unitsConverters.returnTemperatureUsingUserFormat(it.temp.max.toInt().toString())}".also { binding.homeMaxTextView.text = it }
            (getString(R.string.min) +unitsConverters.returnTemperatureUsingUserFormat( it.temp.min.toInt().toString())).also {
                binding.homeMinTextview.text = it

            }
            weatherResponse.let {
                weatherResponse.apply {
                    binding.nameCityTextview.text = timezone
                    binding.homeDateTextView.text = WeatherUtil.dateFormat(current.dt)
                    binding.homeClockTextView.text =
                        WeatherUtil.getHourAndMinute(it.hourly[0].dt.toLong())
                    binding.homeMainTemTextView.text =unitsConverters.returnTemperatureUsingUserFormat( current.temp.toInt().toString())
                    binding.txtVwValueHumidity.text = current.humidity.toString().plus(
                        getString(R.string.precentage)
                    )
                    binding.txtVwValuePressure.text = current.pressure.toString().plus(
                        getString(R.string.pressure_symbol)
                    )
                    binding.txtVwValueSpeed.text = current.wind_speed.toString()
                    binding.txtVwValueCloud.text = current.clouds.toString()
                    binding.feelLikeValueTextview.text = current.feels_like.toString()
                    binding.homeDescriptionTextview.text = current.weather[0].description
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(binding.homeImageImageView)
                            .load(Constants.IMG_URL + current.weather[0].icon + ".png")
                            .into(binding.homeImageImageView)
                    }
                    dailyAdapter.changeData(daily)
                    hourlyAdapter.changeData(hourly)
                }


            }
        }
        binding.animationView.visibility = View.GONE
    }
}
