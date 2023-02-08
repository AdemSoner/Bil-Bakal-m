package com.example.bilbakalim.Model


data class User(val userEmail:String,val userPassword:String)
data class Words(val key:String,
                 val bannedWord1:String,
                 val bannedWord2:String,
                 val bannedWord3:String,
                 val bannedWord4:String,
                 val bannedWord5:String)