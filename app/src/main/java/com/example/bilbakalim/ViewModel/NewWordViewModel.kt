package com.example.bilbakalim.ViewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bilbakalim.Model.Words
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NewWordViewModel : ViewModel() {
    val wordMessage= MutableLiveData<String>()
    val wordInProgress= MutableLiveData<Boolean>()
    val isHave=MutableLiveData<Boolean>()
    val word=MutableLiveData<Words>()


    fun addWordToDatabase(word: Words){
        FirebaseDatabase.getInstance().reference
            .child("Words")
            .child(word.key)
            .setValue(word)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    wordMessage.value="Ekleme başarılı, farklı bir tane daha ekleyebilirsiniz."
                }else{
                    wordMessage.value=it.exception?.message.toString()
                }
                wordInProgress.value=false
            }
    }

    fun checkWord(word: Words) {
        wordInProgress.value = true
        val query = FirebaseDatabase.getInstance().reference
            .child("Words")
            .child(word.key)
            .orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isHave.value = snapshot.value != null
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata Oluştu Database hatası")
            }
        })
    }

}