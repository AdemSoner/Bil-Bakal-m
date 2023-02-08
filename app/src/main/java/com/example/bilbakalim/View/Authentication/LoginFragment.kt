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
import com.example.bilbakalim.Model.User
import com.example.bilbakalim.R
import com.example.bilbakalim.databinding.FragmentLoginBinding
import com.example.bilbakalim.ViewModel.LoginViewModel


class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginBinding.inflate(inflater,container, false)
        viewModel= ViewModelProviders.of(this)[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        observeLiveData(requireView())
    }

    private fun initializeUI() {
        binding.signInButton.setOnClickListener {
            val userEmail= binding.emailText.text.toString()
            val userPassword = binding.passwordLogin.text.toString()
            val user=User(userEmail,userPassword)
            if(userEmail!="" && userPassword !="") viewModel.signInWithFirebase(user)
            else Toast.makeText(context,R.string.fill,Toast.LENGTH_LONG).show()

        }
        binding.signUpTextView.setOnClickListener {
            val action= LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.forgotPasswordTextView.setOnClickListener {
            ForgotPassword().show(childFragmentManager,"forgotPasswordFragment")
        }
    }

    private fun observeLiveData(view:View) {
        viewModel.loginInProgress.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                enableDisableComponents(it)
            }
        })
        viewModel.loginErrorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context,error,Toast.LENGTH_LONG).show()
            }
        })
        viewModel.loginIsSuccess.observe(viewLifecycleOwner, Observer { isSuccess->
            isSuccess?.let {
                if(it){
                    val action= LoginFragmentDirections.actionLoginFragmentToMainPage()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

    }

    private fun enableDisableComponents(value:Boolean) {
        binding.emailText.isEnabled=!value
        binding.passwordLogin.isEnabled=!value
        binding.forgotPasswordTextView.isEnabled=!value
        binding.signInButton.isEnabled=!value
        binding.signUpTextView.isEnabled=!value
        if(!value) binding.loginProgressBar.visibility=View.GONE
        else binding.loginProgressBar.visibility=View.VISIBLE
    }

}