package com.example.zeusweather.ui.favourite

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.zeusweather.R
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.databinding.FragmentFavouriteBinding
import com.example.zeusweather.util.WeatherUtil.checkForInternet
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private var _binding: FragmentFavouriteBinding? = null
    private val viewModel: FavouriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavouriteAdapter
    lateinit var geocoder: Geocoder
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        favoriteAdapter = FavouriteAdapter(arrayListOf(), viewModel)
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        binding.btnFab.setOnClickListener {
            viewModel.addFavouritePlace(it)
        }

        viewModel.getFavouriteListFromRoom(true).asLiveData().observe(viewLifecycleOwner) {
            favoriteAdapter.changeData(it)
        }
//        if (!checkForInternet(requireContext()))
//            binding.btnFab.visibility=View.GONE
        initUI()
        return root
    }

    private fun initUI() {
        binding.rvFavourites.apply {
            adapter = favoriteAdapter
        }
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                favoriteAdapter.removeFromAdapter(viewHolder)
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvFavourites)


        favoriteAdapter.onItemClickListener = object : FavouriteAdapter.OnItemClickListener {
            override fun onClick(pos: Int, weatherResponse: WeatherResponse) {
                viewModel.setDetailsForItemSelected(weatherResponse)
              val  weatherString = Gson().toJson(weatherResponse)
                val bundle = Bundle()
                bundle.putString("weatherString", weatherString)
                findNavController().navigate(R.id.action_navigation_favourite_to_navigation_FavouriteFragment,bundle)
                  }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}