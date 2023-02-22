package com.example.bilbakalim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SplashViewModel:ViewModel() {
    val loading=MutableLiveData<Boolean>()
    val rememberStatus=MutableLiveData<Boolean>()

    private val firebaseAuth=FirebaseAuth.getInstance()
    fun checkRememberMeStatusOnFirebase(){
        loading.value=true
        if(firebaseAuth.currentUser != null){
            FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(firebaseAuth.currentUser?.uid.toString())
                .child("rememberMe")
                .get()
                .addOnSuccessListener {
                    rememberStatus.value=it.value.toString().toBoolean()
                }

        }else{
            rememberStatus.value=false
            loading.value=false
        }

    }
}