package com.example.bilbakalim.View.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.bilbakalim.databinding.FragmentMainPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class MainPage : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI(view)
    }

    private fun initializeUI(view: View) {
        binding.newGameBtn.setOnClickListener {
            val action = MainPageDirections.actionMainPageToGamePreparationFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.settingsBtn.setOnClickListener {
            val action = MainPageDirections.actionMainPageToSettingsFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.addWordBtn.setOnClickListener {
            val action = MainPageDirections.actionMainPageToNewWordFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.quitGameBtn.setOnClickListener {
            setUserRememberMeStatus(FirebaseAuth.getInstance().currentUser)
            FirebaseAuth.getInstance().signOut()
            val action = MainPageDirections.actionMainPageToLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun setUserRememberMeStatus(currentUser: FirebaseUser?) {
        FirebaseDatabase.getInstance().reference.child("Users").child(currentUser?.uid.toString())
            .child("rememberMe").setValue(false)
    }

}