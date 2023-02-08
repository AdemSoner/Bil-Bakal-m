package com.example.bilbakalim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.User
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel:ViewModel() {
    val loginInProgress= MutableLiveData<Boolean>()
    val loginErrorMessage = MutableLiveData<String>()
    val loginIsSuccess = MutableLiveData<Boolean>()

    fun signInWithFirebase(userInfo:User){
        loginInProgress.value=true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            userInfo.userEmail,
            userInfo.userPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    loginIsSuccess.value=true
                }else{
                    loginIsSuccess.value=false
                    loginErrorMessage.value=it.exception?.message.toString()
                }
                loginInProgress.value=false
            }

    }
}