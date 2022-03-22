package com.example.zeusweather.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.zeusweather.R
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment() {


    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels()
    lateinit var locale: Locale
    private var currentLanguage: String? = null
    private var currentLang: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferencesFactory = SharedPreferencesFactory(requireContext())

        currentLanguage = sharedPreferencesFactory.getLanguage()
        when {
            sharedPreferencesFactory.getUnitsOfTemperature().equals("K") -> {
                binding.kRadioButton.isChecked = true
            }
            sharedPreferencesFactory.getUnitsOfTemperature().equals("C") -> {
                binding.cRadioButton.isChecked = true
            }
            sharedPreferencesFactory.getUnitsOfTemperature().equals("F") -> {
                binding.fRadioButton.isChecked = true
            }
        }

        when {
            sharedPreferencesFactory.getNotificationEnabledOrDisabled() -> {
                binding.enableNotificationRadioButton.isChecked = true
            }
            !sharedPreferencesFactory.getNotificationEnabledOrDisabled() -> {
                binding.disableNotificationRadioButton.isChecked = true
            }
        }

        when {
            sharedPreferencesFactory.getUnitsOfWindSpeed().equals("meter/sec") -> {
                binding.meterPerSecondRadioButton.isChecked = true
            }
            sharedPreferencesFactory.getUnitsOfWindSpeed().equals("miles/hour") -> {
                binding.milePerHourRadioButton.isChecked = true
            }
        }

        if (sharedPreferencesFactory.getAlertEnabledOrDisabled()) {
            binding.alertEnableRadioButton.isChecked = true
        } else {
            binding.alertDisableRadioButton.isChecked = true
        }

        val lang = Locale.getDefault().language
        if (lang.equals("en")) {
            binding.englishRadioButton.isChecked = true
        } else if (lang.equals("ar")) {
            binding.arabicRadioButton.isChecked = true
        }

        binding.languageRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.arabicRadioButton) {
                viewModel.saveLanguageInSharedPreferences("ar", requireContext())
                viewModel.updateApplicationLanguage("ar", requireActivity())
            } else if (i == R.id.englishRadioButton) {
                viewModel.saveLanguageInSharedPreferences("en", requireContext())
                viewModel.updateApplicationLanguage("en", requireActivity())
            }
        }

        binding.temperatureRadioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.kRadioButton -> {
                    viewModel.saveTemperatureUnitInSharedPreferences("K", requireContext())
                }
                R.id.cRadioButton -> {
                    viewModel.saveTemperatureUnitInSharedPreferences("C", requireContext())
                }
                R.id.fRadioButton -> {
                    viewModel.saveTemperatureUnitInSharedPreferences("F", requireContext())
                }
            }
        }

        binding.windSpeedRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.milePerHourRadioButton) {
                viewModel.saveWindSpeedUnitInSharedPreferences("miles/hour", requireContext())
            } else if (i == R.id.meterPerSecondRadioButton) {
                viewModel.saveWindSpeedUnitInSharedPreferences("meter/sec", requireContext())
            }
        }

        binding.notificationsRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.enableNotificationRadioButton) {
                viewModel.saveNotificationEnabledOrDisabledInSharedPreferences(
                    true,
                    requireContext()
                )
            } else if (i == R.id.disableNotificationRadioButton) {
                viewModel.saveNotificationEnabledOrDisabledInSharedPreferences(
                    false,
                    requireContext()
                )
            }
        }

        binding.alertDialogRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.alertEnableRadioButton) {
                viewModel.saveAlertEnabledOrDisabledInSharedPreferences(
                    true,
                    requireContext()
                )
            } else if (i == R.id.alertDisableRadioButton) {
                viewModel.saveAlertEnabledOrDisabledInSharedPreferences(
                    false,
                    requireContext()
                )
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}