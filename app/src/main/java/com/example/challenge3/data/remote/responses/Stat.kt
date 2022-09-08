package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)