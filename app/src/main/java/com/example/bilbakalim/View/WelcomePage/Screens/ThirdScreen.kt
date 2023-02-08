package com.example.bilbakalim.View.WelcomePage.Screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bilbakalim.R
import com.example.bilbakalim.databinding.FragmentThirdScreenBinding


class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentThirdScreenBinding.inflate(inflater,container,false)
        binding.btnFinish.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("Finished",true)
            editor.apply()
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }

        return binding.root
    }


}