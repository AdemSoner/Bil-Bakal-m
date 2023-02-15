package com.example.bilbakalim.View.Game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.bilbakalim.R
import com.example.bilbakalim.databinding.FragmentMainPageBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess


class MainPage : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMainPageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI(view)
    }

    private fun initializeUI(view: View) {
        binding.newGameBtn.setOnClickListener {
            val action=MainPageDirections.actionMainPageToGamePreparationFragment()
            Navigation.findNavController(it).navigate(action)

        }
        binding.settingsBtn.setOnClickListener {
            val action= MainPageDirections.actionMainPageToSettingsFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.addWordBtn.setOnClickListener {
            val action= MainPageDirections.actionMainPageToNewWordFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.quitGameBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val action= MainPageDirections.actionMainPageToLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

}