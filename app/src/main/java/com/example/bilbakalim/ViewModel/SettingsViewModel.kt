package com.example.bilbakalim.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.NEWUSERSETTINGS
import com.example.bilbakalim.Model.UserSettings
import com.example.bilbakalim.View.Game.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsViewModel : ViewModel() {
    val settingsInProgress=MutableLiveData<Boolean>()
    val settingsSetSuccess=MutableLiveData<String>()
    val setUserSettingsToTexts=MutableLiveData<Boolean>()

    val pass=MutableLiveData<String>()
    val gameTime= MutableLiveData<String>()
    val winPoint= MutableLiveData<String>()
    private val firebaseAuth = FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference

    fun getUserSettings(){
        settingsInProgress.value=true
        val query = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(firebaseAuth?.uid.toString())
            .child("userSettings")
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pass.value=snapshot.child("pass").value.toString()
                gameTime.value=snapshot.child("gameTime").value.toString()
                winPoint.value=snapshot.child("winningPoint").value.toString()
                setUserSettingsToTexts.value=true
                settingsInProgress.value=false
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })

    }

    fun setDefaultSettings(){
        pass.value= NEWUSERSETTINGS.pass
        gameTime.value= NEWUSERSETTINGS.gameTime
        winPoint.value= NEWUSERSETTINGS.winningPoint
        setUserSettingsToTexts.value=true
        setNewSettings()
    }

    fun setNewSettings(){
        settingsInProgress.value=true
        val userSettings=UserSettings(gameTime.value!!,pass.value!!,winPoint.value!!)
        firebaseDatabase.child("Users")
            .child(firebaseAuth?.uid.toString())
            .child("userSettings")
            .setValue(userSettings)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    settingsSetSuccess.value= "Güncelleme Başarılı"
                    setUserSettingsToTexts.value=true
                } else settingsSetSuccess.value= "Güncelleme Başarısız"

                settingsInProgress.value=false
            }
    }
}