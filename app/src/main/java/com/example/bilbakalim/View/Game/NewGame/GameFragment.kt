package com.example.bilbakalim.View.Game.NewGame

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bilbakalim.Model.Words
import com.example.bilbakalim.R
import com.example.bilbakalim.ViewModel.GameViewModel
import com.example.bilbakalim.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    private var _binding:FragmentGameBinding? = null
    private val binding get()=_binding!!
    lateinit var viewModel:GameViewModel
    private var pauseFlag=0
    private var startFlag=0
    private var pauseTime=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProviders.of(this)[GameViewModel::class.java]
        viewModel.getGameDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiliazeUI()
        observeLiveData()
    }

    private fun initiliazeUI() {
        binding.readyBtn.setOnClickListener {
            binding.readyScreenConstraint.visibility=View.GONE
            binding.gameFragmentContainer.visibility=View.VISIBLE
            setWord(viewModel.nextWord())
            startFlag=1
            viewModel.setGameSettings()
        }
        binding.passBtn.setOnClickListener {
            viewModel.passMedia(requireContext())
            viewModel.passChance.value= (viewModel.passChance.value)?.minus(1)
            setWord(viewModel.nextWord())
        }
        binding.pauseBtn.setOnClickListener {
            pauseTime=viewModel.timer.value!!.minus(1)
            binding.gameFragmentContainer.visibility=View.GONE
            pauseFlag=1
            val alert= AlertDialog.Builder(context)
            alert.setTitle("Oyun duraklatıldı.Devam etmek istiyor musunuz?")
                .setIcon(R.drawable.ic_pause_circle)
                .setPositiveButton("Evet", DialogInterface.OnClickListener{ dialog, which ->
                    pauseFlag=0
                    dialog.cancel()
                    binding.gameFragmentContainer.visibility=View.VISIBLE
                    viewModel.timer.value=pauseTime
                })
                .setNegativeButton("Hayır",DialogInterface.OnClickListener { dialog, which ->
                    val action=GameFragmentDirections.actionGameFragmentToMainPage()
                    Navigation.findNavController(it).navigate(action)
                })
                .setMessage("Kalan süreniz= $pauseTime")
                .create()
                .show()
        }
        binding.trueBtn.setOnClickListener {
            viewModel.score.value= (viewModel.score.value)?.plus(1)
            val gameFinishScore=viewModel.userSettings[2].toInt()
            if(viewModel.team1Score != gameFinishScore && viewModel.team2Score != gameFinishScore)
                viewModel.correctMedia(requireContext())
            setWord(viewModel.nextWord())
        }
        binding.penaltyBtn.setOnClickListener {
            viewModel.score.value= (viewModel.score.value)?.minus(1)
            viewModel.incorrectMedia(requireContext())
            setWord(viewModel.nextWord())
        }

        binding.goMainMenuBtn.setOnClickListener {
            val action=GameFragmentDirections.actionGameFragmentToMainPage()
            Navigation.findNavController(it).navigate(action)
        }
        binding.goMenuBtn.setOnClickListener {
            val action=GameFragmentDirections.actionGameFragmentToMainPage()
            Navigation.findNavController(it).navigate(action)
        }
        binding.nextPlayerBtn.setOnClickListener {
            viewModel.readyToShow.value=false
            viewModel.readyToShow.value=true
            binding.nextPlayerFragmentContainer.visibility=View.GONE
            binding.readyScreenConstraint.visibility=View.VISIBLE
        }

    }


    private fun observeLiveData() {

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                enableDisableComponents(it)
            }
        })
        viewModel.readyToShow.observe(viewLifecycleOwner, Observer { ready->
            ready?.let {
                if (it){
                    val teamNameNow=viewModel.getNextTeam()
                    binding.teamNameTV.text=teamNameNow
                    binding.gameTeamNameTV.text=teamNameNow
                }

            }
        })
        viewModel.timer.observe(viewLifecycleOwner, Observer {time->
            time?.let {
                binding.timerText.text = it.toString()
                if(startFlag==1 && it>0 && pauseFlag==0)
                    viewModel.startTimer()
                else if (startFlag==1 && it>0 && pauseFlag==1){
                    pauseTime=it
                }
                else {
                    startFlag=0
                    viewModel.writeScoreToFirebase()
                    binding.Team1ScoreNextTV.text = "${viewModel.teams[0]}=${viewModel.team1Score}"
                    binding.Team2ScoreNextTV.text = "${viewModel.teams[1]}=${viewModel.team2Score}"
                    binding.gameFragmentContainer.visibility=View.GONE
                    binding.readyScreenConstraint.visibility=View.GONE
                    binding.endGameFragmentContainer.visibility=View.GONE
                    binding.nextPlayerFragmentContainer.visibility=View.VISIBLE
                }
            }
        })
        viewModel.passChance.observe(viewLifecycleOwner, Observer { pass->
            pass?.let {
                binding.passChanceTV.text = it.toString()
                binding.passBtn.isEnabled = it != 0
                }
        })
        viewModel.score.observe(viewLifecycleOwner, Observer { score->
            score?.let {
                viewModel.writeScoreToFirebase()
                if (it==viewModel.userSettings[2].toInt())
                    viewModel.finishGame.value=true
                else
                    binding.scoreTV.text=it.toString()


            }
        })
        viewModel.finishGame.observe(viewLifecycleOwner, Observer {value->
            value?.let {
                if(it){
                    viewModel.finishMedia(requireContext())
                    pauseFlag=1
                    if(viewModel.team1Score == viewModel.userSettings[2].toInt())
                        binding.noWordMessageTV.text="${viewModel.teams[0]} "+getString(R.string.winnerGame)
                    else
                        binding.noWordMessageTV.text="${viewModel.teams[1]} "+getString(R.string.winnerGame)
                    binding.Team1ScoreTV.text = "${viewModel.teams[0]}=${viewModel.team1Score}"
                    binding.Team2ScoreTV.text = "${viewModel.teams[1]}=${(viewModel.team2Score)}"
                    binding.readyScreenConstraint.visibility=View.GONE
                    binding.nextPlayerFragmentContainer.visibility=View.GONE
                    binding.gameFragmentContainer.visibility=View.GONE
                    binding.endGameFragmentContainer.visibility=View.VISIBLE
                }
            }

        })

    }

    private fun enableDisableComponents(value: Boolean) {
        binding.readyBtn.isEnabled=!value
        binding.trueBtn.isEnabled=!value
        binding.penaltyBtn.isEnabled=!value
        binding.passBtn.isEnabled=!value
        binding.pauseBtn.isEnabled=!value
        if(value) binding.gameProgressBar.visibility=View.VISIBLE
        else binding.gameProgressBar.visibility=View.GONE
    }
    private fun setWord(word: Words) {
        var spanString= SpannableString(word.key)
        spanString.setSpan(UnderlineSpan(),0,spanString.length, 0)
        binding.keyTV.text=spanString
        binding.banWord1TV.text=word.bannedWord1
        binding.banWord2TV.text=word.bannedWord2
        binding.banWord3TV.text=word.bannedWord3
        binding.banWord4TV.text=word.bannedWord4
        binding.banWord5TV.text=word.bannedWord5
    }

}