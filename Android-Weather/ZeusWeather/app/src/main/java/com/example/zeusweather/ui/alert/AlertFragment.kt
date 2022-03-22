package com.example.zeusweather.ui.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.zeusweather.databinding.FragmentAlertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null

    private val viewModel: AlertViewModel by viewModels()

    lateinit var alertAdapter: AlertAdapter


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addNewAlertFabBtn = binding.addNewAlert
        addNewAlertFabBtn.setOnClickListener {
            AddingAlertDialog().show(childFragmentManager, "NewAlertDialog")
        }
        alertAdapter = AlertAdapter(arrayListOf(),viewModel)
        initAlertsRecycler()
        getAlertsFromViewModel()


        return root
    }

    private fun initAlertsRecycler() {
        binding.alertsRecyclerView.apply {
            adapter = alertAdapter
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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
                alertAdapter.removeSelectedAlertFromAdapter(viewHolder)
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.alertsRecyclerView)

    }


    private fun getAlertsFromViewModel() {
        viewModel.getAlerts().asLiveData().observe(viewLifecycleOwner) {
            alertAdapter.changeData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}