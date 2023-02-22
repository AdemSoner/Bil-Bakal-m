package com.example.bilbakalim.View.WelcomePage

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bilbakalim.R
import com.example.bilbakalim.ViewModel.SplashViewModel
import com.example.bilbakalim.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[SplashViewModel::class.java]
        Handler().postDelayed({
            if (onBoardingFinished()) {
                viewModel.checkRememberMeStatusOnFirebase()
            } else {
                val action = SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                Navigation.findNavController(binding.root).navigate(action)
            }
        }, 1500)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun observeLiveData() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                enableDisableComponents(it)
            }
        })
        viewModel.rememberStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                if (it) {
                    val action = SplashFragmentDirections.actionSplashFragmentToMainPage()
                    Navigation.findNavController(binding.root).navigate(action)
                } else {
                    val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    Navigation.findNavController(binding.root).navigate(action)
                }


            }
        })
    }

    private fun enableDisableComponents(value: Boolean) {
        if (value) binding.splashProgress.visibility = View.VISIBLE
        else binding.splashProgress.visibility = View.GONE
    }


}