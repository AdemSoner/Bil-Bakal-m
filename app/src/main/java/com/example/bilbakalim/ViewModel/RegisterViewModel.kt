package com.example.bilbakalim.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel() : ViewModel() {
    val registerErrorMessage= MutableLiveData<String>()
    val registerInProgress=MutableLiveData<Boolean>()
    val registerIsSuccess=MutableLiveData<Boolean>()
    private val firebaseAuth= FirebaseAuth.getInstance()

    fun signUpWithFirebase(userInformation:User, context:Context?){
        registerInProgress.value=true
        try {
            firebaseAuth
                .createUserWithEmailAndPassword(
                    userInformation.userEmail,
                    userInformation.userPassword)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        registerIsSuccess.value=true
                        addDatabase(userInformation,context)
                        firebaseAuth.signOut()
                    }else{
                        registerIsSuccess.value=false
                        registerErrorMessage.value=it.exception?.message.toString()
                    }
                    registerInProgress.value=false
                }

        }catch (e:Exception){
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
        }


    }

    private fun addDatabase(user:User, context:Context?) {
        try {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference
            val userUID=firebaseAuth.currentUser?.uid.toString()
            firebaseDatabase.child("Users")
                .child(userUID)
                .setValue(user)
            firebaseDatabase.child("Users")
                .child(userUID)
                .child("UID")
                .setValue(userUID)

        }catch (e:Exception){
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
        }


    }
}