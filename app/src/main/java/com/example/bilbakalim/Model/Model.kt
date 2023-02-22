package com.example.bilbakalim.Model

import com.google.gson.annotations.SerializedName


data class User(
    val userEmail: String,
    val userPassword: String,
    val userSettings: UserSettings,
    val rememberMe: Boolean
)

data class Words(
    @SerializedName("key")
    val key: String,
    @SerializedName("bannedWord1")
    val bannedWord1: String,
    @SerializedName("bannedWord2")
    val bannedWord2: String,
    @SerializedName("bannedWord3")
    val bannedWord3: String,
    @SerializedName("bannedWord4")
    val bannedWord4: String,
    @SerializedName("bannedWord5")
    val bannedWord5: String

)

data class UserSettings(val gameTime: String, val pass: String, val winningPoint: String)

data class Teams(val teamNameOne: String, val teamNameTwo: String)
