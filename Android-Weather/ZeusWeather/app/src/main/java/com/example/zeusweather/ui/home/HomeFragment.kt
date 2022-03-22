package com.example.zeusweather.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.zeusweather.R
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.databinding.FragmentHomeBinding
import com.example.zeusweather.util.Constants.IMG_URL
import com.example.zeusweather.util.Constants.PERMISSION_REQUEST_ACCESS_LOCATION
import com.example.zeusweather.util.UnitsConverters
import com.example.zeusweather.util.WeatherUtil
import com.example.zeusweather.util.WeatherUtil.checkForInternet
import com.example.zeusweather.util.WeatherUtil.dateFormat
import com.example.zeusweather.util.WeatherUtil.getHourAndMinute
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    private val viewModel: HomeViewModel by viewModels()

    private val binding get() = _binding!!


    lateinit var weatherResponse: WeatherResponse
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    lateinit var currentCity: String
    lateinit var unitsConverters: UnitsConverters
    lateinit var lang: String
    var currentLat: Double = 0.0
    var currentLon: Double = 0.0
    lateinit var sharedPreferencesFactory: SharedPreferencesFactory
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lang = WeatherUtil.getCurrentLanguage(requireContext())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferencesFactory = SharedPreferencesFactory(requireContext())
        unitsConverters = UnitsConverters(requireContext())
        hourlyAdapter = HourlyAdapter(arrayListOf())
        dailyAdapter = DailyAdapter(arrayListOf())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        initRecyclers()



        if (checkForInternet(requireContext())) {
            binding.animationView.visibility = View.VISIBLE
            lifecycle.coroutineScope.launch {
                getLastLocation()
            }

        } else {
            binding.animationView.visibility = View.VISIBLE
            viewModel.getDataFromRoom().asLiveData().observe(viewLifecycleOwner) {
                if (it != null)
                    updateUI(it, it.timezone)
                else {
                    Toast.makeText(context, getString(R.string.open_internet), Toast.LENGTH_SHORT)
                        .show()
                }
            }

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


    private fun updateUI(weatherResponse: WeatherResponse, currentCity: String) {

            if (weatherResponse != null) {
                weatherResponse.daily[0].let {
                    "${getString(R.string.max)}${
                        unitsConverters.returnTemperatureUsingUserFormat(
                            it.temp.max.toInt().toString()
                        )
                    }".also {
                        binding.homeMaxTextView.text = it
                    }
                    (getString(R.string.min) + unitsConverters.returnTemperatureUsingUserFormat(
                        it.temp.min.toInt().toString()
                    )).also {
                        binding.homeMinTextview.text =
                            it
                    }
                    weatherResponse.let {
                        weatherResponse.apply {
                            binding.nameCityTextview.text = currentCity.split("Governorate")[0]
                            binding.homeDateTextView.text = dateFormat(current.dt)
                            binding.homeClockTextView.text =
                                getHourAndMinute(it.hourly[0].dt.toLong())
                            binding.homeMainTemTextView.text =
                                unitsConverters.returnTemperatureUsingUserFormat(
                                    current.temp.toInt().toString()
                                )
                            binding.txtVwValueHumidity.text = current.humidity.toString().plus(
                                getString(R.string.precentage)
                            )
                            binding.txtVwValuePressure.text = current.pressure.toString().plus(
                                getString(R.string.pressure_symbol)
                            )

                            binding.txtVwValueSpeed.text =
                                unitsConverters.returnWindSpeedUsingUserFormat(current.wind_speed.toString())
                            binding.txtVwValueCloud.text = current.clouds.toString()
                            binding.feelLikeValueTextview.text =
                                unitsConverters.returnTemperatureUsingUserFormat(
                                    current.feels_like.toInt().toString()
                                )
                            binding.homeDescriptionTextview.text = current.weather[0].description
                            lifecycle.coroutineScope.launch {
                                Glide.with(binding.homeImageImageView)
                                    .load(IMG_URL + current.weather[0].icon + "@2x.png")
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

    suspend fun <T> Task<T>.awaitResult() = suspendCoroutine<T?> { continuation ->
        if (isComplete) {
            if (isSuccessful) continuation.resume(result)
            else continuation.resume(null)
            return@suspendCoroutine
        }
        addOnSuccessListener { continuation.resume(result) }
        addOnFailureListener { continuation.resume(null) }
        addOnCanceledListener { continuation.resume(null) }
    }


    private suspend fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("TAG", "checkSelfPermission: ")
                             return
                }
                mFusedLocationClient.lastLocation.awaitResult().let {
                    if (it == null) {
                        requestNewLocationData()
                    } else {

                        currentLat = it.latitude
                        currentLon = it.longitude
                        address = geocoder.getFromLocation(currentLat, currentLon, 1)
                        currentCity = address[0].subAdminArea
                        viewLifecycleOwner.lifecycleScope.launch {
                            weatherResponse =
                                viewModel.makeRequest(currentLat, currentLon, currentCity, lang)
                                    .body()!!
                            updateUI(weatherResponse, currentCity)
                            binding.animationView.visibility = View.GONE
                            val latlng = LatLng(currentLat, currentLon)

                            sharedPreferencesFactory.setLatLng(latlng)
                        }

                    }
                }
            } else {
                locationNotEnable()
            }
        } else {
            requestPermissions()
        }

    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun locationNotEnable() {

        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        ActivityCompat.startActivityForResult(
            requireActivity(),
            intent,
            PERMISSION_REQUEST_ACCESS_LOCATION,
            Bundle()
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                lifecycle.coroutineScope.launch {
                    getLastLocation()
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}