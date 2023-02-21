package com.example.bilbakalim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GamePreparationViewModel:ViewModel() {
    val preparationInProgress=MutableLiveData<Boolean>()
    val gameReadyToStart= MutableLiveData<Boolean>()
    val message=MutableLiveData<String>()

    private val firebaseAuth= FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase=FirebaseDatabase.getInstance().reference
    val wordList=ArrayList<Words>()

    fun startGame(teamOneName:String,teamTwoName:String){
        preparationInProgress.value=true
        val teams=Teams(teamOneName,teamTwoName)
        firebaseDatabase.child("Game")
            .child(firebaseAuth?.uid.toString())
            .child("Teams")
            .setValue(teams)
        firebaseDatabase.child("Game")
            .child(firebaseAuth?.uid.toString())
            .child("Score")
            .child("teamNameOne")
            .setValue("0")
        firebaseDatabase.child("Game")
            .child(firebaseAuth?.uid.toString())
            .child("Score")
            .child("teamNameTwo")
            .setValue("0")

        val query = FirebaseDatabase.getInstance().reference
            .child("Words")
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        val key=singleSnapshot.child("key").value.toString()
                        val bannedWordOne=singleSnapshot.child("bannedWord1").value.toString()
                        val bannedWordTwo=singleSnapshot.child("bannedWord2").value.toString()
                        val bannedWordThree=singleSnapshot.child("bannedWord3").value.toString()
                        val bannedWordFour=singleSnapshot.child("bannedWord4").value.toString()
                        val bannedWordFive=singleSnapshot.child("bannedWord5").value.toString()
                        wordList.add(Words(key,
                            bannedWordOne,
                            bannedWordTwo,
                            bannedWordThree,
                            bannedWordFour,
                            bannedWordFive))
                    }
                }
                firebaseDatabase.child("Game")
                    .child(firebaseAuth?.uid.toString())
                    .child("Words")
                    .setValue(wordList)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            message.value="Oyun başlıyor..."
                            gameReadyToStart.value=true
                        }else
                            message.value="Oyun başlatma hatalı"
                        preparationInProgress.value=false
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })

    }


}