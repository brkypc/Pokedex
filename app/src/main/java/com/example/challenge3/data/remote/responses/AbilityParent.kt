package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class AbilityParent(
    @SerializedName("ability") val ability: Ability,
    @SerializedName("is_hidden") val isHidden: Boolean
)