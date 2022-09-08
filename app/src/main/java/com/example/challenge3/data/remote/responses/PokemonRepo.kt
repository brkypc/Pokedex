package com.example.challenge3.data.remote.responses

import com.google.gson.annotations.SerializedName

data class PokemonRepo(
    @SerializedName("count") val count: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<Result>
)