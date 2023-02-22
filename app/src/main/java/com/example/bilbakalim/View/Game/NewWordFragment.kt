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
import com.example.bilbakalim.Model.Words
import com.example.bilbakalim.R
import com.example.bilbakalim.ViewModel.NewWordViewModel
import com.example.bilbakalim.databinding.FragmentNewWordBinding


class NewWordFragment : Fragment() {
    private var _binding: FragmentNewWordBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewWordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewWordBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[NewWordViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI(view)
        observeLiveData(view)
    }

    private fun initializeUI(view: View) {
        binding.addWordBtn.setOnClickListener {
            val word = binding.mainWord.text.toString()
            val banWord1 = binding.banWord1.text.toString()
            val banWord2 = binding.banWord2.text.toString()
            val banWord3 = binding.banWord3.text.toString()
            val banWord4 = binding.banWord4.text.toString()
            val banWord5 = binding.banWord5.text.toString()
            if (word != "" && banWord1 != "" && banWord2 != "" && banWord3 != "" && banWord4 != "" && banWord5 != "") {
                viewModel.word.value = Words(word, banWord1, banWord2, banWord3, banWord4, banWord5)
                viewModel.checkWord(viewModel.word.value!!)
            } else Toast.makeText(context, R.string.fill, Toast.LENGTH_LONG).show()


        }
        binding.clearText.setOnClickListener {
            binding.mainWord.setText("")
            binding.banWord1.setText("")
            binding.banWord2.setText("")
            binding.banWord3.setText("")
            binding.banWord4.setText("")
            binding.banWord5.setText("")
        }
        binding.goBack.setOnClickListener {
            val action = NewWordFragmentDirections.actionNewWordFragmentToMainPage()
            Navigation.findNavController(view).navigate(action)
        }

    }


    private fun observeLiveData(view: View) {
        viewModel.wordInProgress.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) enableDisableComponents(it)
                else enableDisableComponents(it)
            }
        })
        viewModel.wordMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.isHave.observe(viewLifecycleOwner, Observer { have ->
            have?.let {
                if (it) {
                    viewModel.wordInProgress.value = false
                    Toast.makeText(context, R.string.wordHave, Toast.LENGTH_LONG).show()
                } else {
                    viewModel.addWordToDatabase(viewModel.word.value!!)
                }


            }
        })
    }

    private fun enableDisableComponents(value: Boolean) {
        binding.mainWord.isEnabled = !value
        binding.banWord1.isEnabled = !value
        binding.banWord2.isEnabled = !value
        binding.banWord3.isEnabled = !value
        binding.banWord4.isEnabled = !value
        binding.banWord5.isEnabled = !value
        binding.clearText.isEnabled = !value
        binding.goBack.isEnabled = !value
        binding.addWordBtn.isEnabled = !value
        if (!value) binding.wordProgressBar.visibility = View.GONE
        else binding.wordProgressBar.visibility = View.VISIBLE

    }


}