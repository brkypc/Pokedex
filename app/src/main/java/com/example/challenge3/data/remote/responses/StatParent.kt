package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class StatParent(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: Stat
)