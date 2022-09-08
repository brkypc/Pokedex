package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("other")
    val other: Other?,
)