package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Ability (
    @SerializedName("name") val name: String? = null,
    @SerializedName("url") val url: String? = null
)