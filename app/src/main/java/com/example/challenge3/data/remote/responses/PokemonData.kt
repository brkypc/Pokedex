package com.example.challenge3.data.remote.responses


import com.google.gson.annotations.SerializedName

data class PokemonData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("is_default") val isDefault: Boolean,
    @SerializedName("abilities") val abilities: List<AbilityParent>,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("stats") val stats: List<StatParent>,
    @SerializedName("types") val types: List<TypeParent>
)