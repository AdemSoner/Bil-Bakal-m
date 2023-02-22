package com.example.bilbakalim.Service

import com.example.bilbakalim.Model.Words
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WordsAPIService {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WordsAPI::class.java)

    fun getWordsFromAPI(): Call<List<Words>> {
        return api.getWordsFromInternet()
    }
}