package com.example.bilbakalim.Service


import com.example.bilbakalim.Model.Words
import retrofit2.Call
import retrofit2.http.GET

interface WordsAPI {
    @GET(CONTENT_API)
    fun getWordsFromInternet(): Call<List<Words>>
}