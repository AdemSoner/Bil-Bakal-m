package com.example.bilbakalim.View.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bilbakalim.ViewModel.SettingsViewModel
import com.example.bilbakalim.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: SettingsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[SettingsViewModel::class.java]
        viewModel.getUserSettings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalizeUI(view)
        observeLiveData()
    }

    private fun initalizeUI(view: View) {
        binding.defaultSettingsBtn.setOnClickListener {
            viewModel.setDefaultSettings()
        }
        binding.goBackBtn.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToMainPage()
            Navigation.findNavController(view).navigate(action)
        }
        // ------------------------------------------------
        binding.passOneBtn.setOnClickListener {
            viewModel.pass.value = "1"
            viewModel.setNewSettings()
        }
        binding.passTwoBtn.setOnClickListener {
            viewModel.pass.value = "2"
            viewModel.setNewSettings()
        }
        binding.passThreeBtn.setOnClickListener {
            viewModel.pass.value = "3"
            viewModel.setNewSettings()
        }
        // ------------------------------------------------
        binding.timeOneBtn.setOnClickListener {
            viewModel.gameTime.value = "30"
            viewModel.setNewSettings()
        }
        binding.timeTwoBtn.setOnClickListener {
            viewModel.gameTime.value = "60"
            viewModel.setNewSettings()
        }
        binding.timeThreeBtn.setOnClickListener {
            viewModel.gameTime.value = "90"
            viewModel.setNewSettings()

        }
        // ------------------------------------------------
        binding.winPointOneBtn.setOnClickListener {
            viewModel.winPoint.value = "10"
            viewModel.setNewSettings()
        }
        binding.winPointTwoBtn.setOnClickListener {
            viewModel.winPoint.value = "25"
            viewModel.setNewSettings()
        }
        binding.winPointThreeBtn.setOnClickListener {
            viewModel.winPoint.value = "40"
            viewModel.setNewSettings()
        }
    }

    private fun observeLiveData() {
        viewModel.settingsInProgress.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) enableDisableComponents(true)
                else enableDisableComponents(false)
            }
        })
        viewModel.settingsSetSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            isSuccess?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.setUserSettingsToTexts.observe(viewLifecycleOwner, Observer { value ->
            value?.let {
                if (it) {
                    binding.timeText.text = viewModel.gameTime.value
                    binding.winPointText.text = viewModel.winPoint.value
                    binding.passText.text = viewModel.pass.value
                    viewModel.setUserSettingsToTexts.value = false
                }
            }
        })
    }

    private fun enableDisableComponents(value: Boolean) {
        binding.passOneBtn.isEnabled = !value
        binding.passTwoBtn.isEnabled = !value
        binding.passThreeBtn.isEnabled = !value
        binding.timeOneBtn.isEnabled = !value
        binding.timeTwoBtn.isEnabled = !value
        binding.timeThreeBtn.isEnabled = !value
        binding.winPointTwoBtn.isEnabled = !value
        binding.winPointOneBtn.isEnabled = !value
        binding.winPointThreeBtn.isEnabled = !value
        binding.defaultSettingsBtn.isEnabled = !value
        binding.goBackBtn.isEnabled = !value
        if (value) binding.settingsProgressBar.visibility = View.VISIBLE
        else binding.settingsProgressBar.visibility = View.GONE

    }


}



