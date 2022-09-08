package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class TypeParent(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: Type
)