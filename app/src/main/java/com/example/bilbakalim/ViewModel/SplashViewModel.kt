package com.example.bilbakalim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.Words
import com.example.bilbakalim.Service.WordsAPIService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val rememberStatus = MutableLiveData<Boolean>()

    private val wordApiService = WordsAPIService()

    private val firebaseAuth = FirebaseAuth.getInstance()
    fun checkRememberMeStatusOnFirebase() {
        loading.value = true
        if (firebaseAuth.currentUser != null) {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(firebaseAuth.currentUser?.uid.toString()).child("rememberMe").get()
                .addOnSuccessListener {
                    rememberStatus.value = it.value.toString().toBoolean()
                }

        } else {
            rememberStatus.value = false
            loading.value = false
        }

    }


    fun refreshWordsFromAPI() {
        val apiCall = wordApiService.getWordsFromAPI()
        apiCall.enqueue(object : Callback<List<Words>> {
            override fun onResponse(call: Call<List<Words>>, response: Response<List<Words>>) {
                val sizeResponse = response.body()?.size!!
                for (a in 0 until sizeResponse - 5) {
                    val key = response.body()?.get(a)?.key!!
                    val bannedWord1 = response.body()?.get(a)?.bannedWord1!!
                    val bannedWord2 = response.body()?.get(a)?.bannedWord2!!
                    val bannedWord3 = response.body()?.get(a)?.bannedWord3!!
                    val bannedWord4 = response.body()?.get(a)?.bannedWord4!!
                    val bannedWord5 = response.body()?.get(a)?.bannedWord5!!
                    if (key != "null"
                        && bannedWord1 != ""
                        && bannedWord2 != ""
                        && bannedWord3 != ""
                        && bannedWord4 != ""
                        && bannedWord5 != "") {
                        val word = Words(key,
                            bannedWord1,
                            bannedWord2,
                            bannedWord3,
                            bannedWord4,
                            bannedWord5)
                        checkWordInFirebase(word)
                    }

                }
            }

            override fun onFailure(call: Call<List<Words>>, t: Throwable) {
                println("Hata: ${t.message}")
            }

        })

    }

    private fun checkWordInFirebase(word: Words) {
        FirebaseDatabase.getInstance().reference.child("Words").child(word.key).child("key").get()
            .addOnSuccessListener {
                if (it.value.toString() != word.key) {
                    addWordToFirebase(word)
                }
            }.addOnFailureListener { println("Hata, ${it.message}") }
    }

    private fun addWordToFirebase(word: Words) {
        FirebaseDatabase.getInstance().reference.child("Words").child(word.key).setValue(word)
    }
}