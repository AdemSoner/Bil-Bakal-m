package com.example.bilbakalim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginViewModel : ViewModel() {
    val loginInProgress = MutableLiveData<Boolean>()
    val loginErrorMessage = MutableLiveData<String>()
    val loginIsSuccess = MutableLiveData<Boolean>()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signInWithFirebase(userInfo: User) {
        loginInProgress.value = true
        firebaseAuth.signInWithEmailAndPassword(
            userInfo.userEmail,
            userInfo.userPassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    setRememberMeChecked(userInfo.rememberMe)
                    loginIsSuccess.value = true
                } else {
                    loginIsSuccess.value = false
                    loginErrorMessage.value = it.exception?.message.toString()
                }
                loginInProgress.value = false
            }

    }

    private fun setRememberMeChecked(rememberMe: Boolean) {
        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(firebaseAuth.currentUser?.uid.toString())
            .child("rememberMe")
            .setValue(rememberMe)
    }
}