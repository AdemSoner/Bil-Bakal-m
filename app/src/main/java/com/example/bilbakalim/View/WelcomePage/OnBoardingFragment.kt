package com.example.bilbakalim.View.WelcomePage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bilbakalim.Adapter.OnBoardingAdapter
import com.example.bilbakalim.View.WelcomePage.Screens.*

import com.example.bilbakalim.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    private var _binding:FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOnBoardingBinding.inflate(inflater,container,false)
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.onBoardingViewPager.adapter= adapter

        return binding.root
    }

}