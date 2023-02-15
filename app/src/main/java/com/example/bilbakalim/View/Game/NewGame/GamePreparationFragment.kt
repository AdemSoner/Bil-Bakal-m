package com.example.bilbakalim.View.Game.NewGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bilbakalim.R
import com.example.bilbakalim.ViewModel.GamePreparationViewModel
import com.example.bilbakalim.databinding.FragmentGamePreparationBinding


class GamePreparationFragment : Fragment() {
    private var _binding : FragmentGamePreparationBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel:GamePreparationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentGamePreparationBinding.inflate(inflater,container,false)
        viewModel=ViewModelProviders.of(this)[GamePreparationViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        observeLiveData(view)
    }

    private fun initializeUI() {
        binding.goMenuBtn.setOnClickListener {view->
            val action=GamePreparationFragmentDirections.actionGamePreparationFragmentToMainPage()
            Navigation.findNavController(view).navigate(action)
        }
        binding.clearText.setOnClickListener {
            binding.teamOneNameEditT.setText("")
            binding.teamTwoNameEditT.setText("")

        }
        binding.startGameBtn.setOnClickListener {
            val teamOneName=binding.teamOneNameEditT.text.toString()
            val teamTwoName=binding.teamTwoNameEditT.text.toString()
            if(teamOneName!="" && teamTwoName!=""){
                viewModel.startGame(teamOneName,teamTwoName)
            }

        }

    }

    private fun observeLiveData(view: View) {
        viewModel.preparationInProgress.observe(viewLifecycleOwner, Observer { loading->
            loading?.let{
                if (it) enableDisableComponents(it)
                else enableDisableComponents(it)
            }
        })
        viewModel.gameReadyToStart.observe(viewLifecycleOwner, Observer { ready->
            ready?.let {
                if (it){
                    val action= GamePreparationFragmentDirections.actionGamePreparationFragmentToGameFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })
        viewModel.message.observe(viewLifecycleOwner, Observer { message->
            message?.let {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun enableDisableComponents(value: Boolean) {
        binding.goMenuBtn.isEnabled=!value
        binding.teamOneNameEditT.isEnabled=!value
        binding.teamTwoNameEditT.isEnabled=!value
        binding.clearText.isEnabled=!value
        binding.startGameBtn.isEnabled=!value
        if(value) binding.preparationProgressBar.visibility=View.VISIBLE
        else binding.preparationProgressBar.visibility=View.GONE
    }

}