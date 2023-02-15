package com.example.bilbakalim.Model


data class User(val userEmail:String,val userPassword:String,val userSettings:UserSettings)

data class Words(val key:String,
                 val bannedWord1:String,
                 val bannedWord2:String,
                 val bannedWord3:String,
                 val bannedWord4:String,
                 val bannedWord5:String)

data class UserSettings(val gameTime:String, val pass:String, val winningPoint:String)

data class Teams(val teamNameOne:String, val teamNameTwo:String)