package com.example.bilbakalim.ViewModel


import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.Words
import com.example.bilbakalim.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GameViewModel : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val readyToShow = MutableLiveData<Boolean>()
    val timer = MutableLiveData<Int>()
    val passChance = MutableLiveData<Int>()
    val score = MutableLiveData<Int>()
    val finishGame = MutableLiveData<Boolean>()

    var team1Score = 0
    var team2Score = 0

    val teams = ArrayList<String>()
    val userSettings = ArrayList<String>()
    val words = ArrayList<Words>()
    private val temporaryWords = ArrayList<Words>()
    private var countTeam = 0
    private var countWord = 0
    private var temporaryWordCount = 0

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance().currentUser


    fun getGameDetails() {
        loading.value = true
        val query =
            firebaseDatabase.child("Game").child(firebaseAuth?.uid.toString()).child("Teams")
                .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        teams.add(singleSnapshot.value.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })

        val query1 = firebaseDatabase.child("Users").child(firebaseAuth?.uid.toString())
            .child("userSettings").orderByKey()
        query1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        userSettings.add(singleSnapshot.value.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })

        val query2 =
            firebaseDatabase.child("Game").child(firebaseAuth?.uid.toString()).child("Words")
                .orderByKey()
        query2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        countWord++
                        words.add(
                            Words(
                                singleSnapshot.child("key").value.toString(),
                                singleSnapshot.child("bannedWord1").value.toString(),
                                singleSnapshot.child("bannedWord2").value.toString(),
                                singleSnapshot.child("bannedWord3").value.toString(),
                                singleSnapshot.child("bannedWord4").value.toString(),
                                singleSnapshot.child("bannedWord5").value.toString()
                            )
                        )
                    }
                }
                setTemproraryWords(countWord, words)
                readyToShow.value = true
                loading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })
    }


    fun getNextTeam(): String {
        if (countTeam == 2) countTeam = 0
        return teams[countTeam++]
    }

    fun nextWord(): Words {
        return if (countWord == 1) {
            val sendWord = words[0]
            words.remove(sendWord)
            countWord = temporaryWordCount
            for (nowWord in temporaryWords) {
                words.add(nowWord)
            }
            sendWord
        } else {
            val randomWord = (Math.random() * countWord--).toInt()
            val sendWord = words[randomWord]
            words.remove(words[randomWord])
            sendWord
        }
    }

    fun startTimer() {
        Handler().postDelayed({
            timer.value = (timer.value.toString().toInt()) - 1
        }, 1150)

    }

    fun setGameSettings() {
        timer.value = userSettings[0].toInt()
        passChance.value = userSettings[1].toInt()
        readScoreToFirebase(countTeam)
    }


    fun correctMedia(context: Context) {
        MediaPlayer.create(context, R.raw.correct).start()
    }

    fun incorrectMedia(context: Context) {
        MediaPlayer.create(context, R.raw.incorrect).start()
    }

    fun passMedia(context: Context) {
        MediaPlayer.create(context, R.raw.pass).start()
    }

    fun finishMedia(context: Context) {
        MediaPlayer.create(context, R.raw.finish).start()
    }


    fun writeScoreToFirebase() {
        if (countTeam == 1) {
            team1Score = score.value!!
            firebaseDatabase.child("Game").child(firebaseAuth?.uid.toString()).child("Score")
                .child("teamNameOne").setValue(score.value.toString())
        } else {
            team2Score = score.value!!
            firebaseDatabase.child("Game").child(firebaseAuth?.uid.toString()).child("Score")
                .child("teamNameTwo").setValue(score.value.toString())
        }


    }

    private fun readScoreToFirebase(team: Int) {
        if (team == 1) {
            firebaseDatabase.child("Game").child(firebaseAuth?.uid.toString()).child("Score")
                .child("teamNameOne").get().addOnSuccessListener {
                    score.value = it.value.toString().toInt()
                }
        } else {
            firebaseDatabase.child("Game").child(firebaseAuth?.uid.toString()).child("Score")
                .child("teamNameTwo").get().addOnSuccessListener {
                    score.value = it.value.toString().toInt()
                }
        }

    }

    private fun setTemproraryWords(count: Int, word: java.util.ArrayList<Words>) {
        temporaryWordCount = count
        for (nowWord in word) {
            temporaryWords.add(nowWord)
        }
    }

}