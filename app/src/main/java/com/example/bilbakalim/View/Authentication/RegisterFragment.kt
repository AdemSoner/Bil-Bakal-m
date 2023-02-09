package com.example.bilbakalim.View.Authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bilbakalim.Model.NEWUSERSETTINGS
import com.example.bilbakalim.Model.User
import com.example.bilbakalim.R
import com.example.bilbakalim.ViewModel.RegisterViewModel
import com.example.bilbakalim.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get()= _binding!!
    lateinit var viewModel:RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentRegisterBinding.inflate(inflater,container,false)
        viewModel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI(view)
        observeLiveData(view)
    }


    private fun initializeUI(view: View) {
        binding.signUpButton.setOnClickListener {
            val userEmail=binding.emailText.text.toString()
            val userPassword=binding.passwordRegister.text.toString()
            val userPasswordCheck=binding.passwordCheckRegister.text.toString()
            if(userEmail!=""&& userPassword!="" && userPasswordCheck!="")
                if(userPassword==userPasswordCheck) {
                    val user=User(userEmail,userPassword, NEWUSERSETTINGS)
                    viewModel.signUpWithFirebase(user,context)
                }  else Toast.makeText(context, R.string.passNotMatch,Toast.LENGTH_LONG).show()

            else Toast.makeText(context, R.string.fill,Toast.LENGTH_LONG).show()
        }
        binding.goBackBtn.setOnClickListener {
            val action=RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun observeLiveData(view:View) {
        viewModel.registerInProgress.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                enableDisableComponents(it)
            }
        })
        viewModel.registerErrorMessage.observe(viewLifecycleOwner, Observer { message->
            message?.let {
                Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.registerIsSuccess.observe(viewLifecycleOwner, Observer { isSuccess->
            isSuccess?.let {
                if (it){
                    Toast.makeText(context,R.string.registerSuccess,Toast.LENGTH_LONG).show()
                    val action=RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })
    }

    private fun enableDisableComponents(value: Boolean) {
        binding.signUpButton.isEnabled=!value
        binding.passwordRegister.isEnabled=!value
        binding.emailText.isEnabled=!value
        binding.passwordCheckRegister.isEnabled=!value
        binding.goBackBtn.isEnabled=!value
        if(!value) binding.registerProgressBar.visibility=View.GONE
        else binding.registerProgressBar.visibility=View.VISIBLE
    }

}