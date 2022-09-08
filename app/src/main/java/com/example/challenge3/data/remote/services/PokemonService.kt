package com.example.challenge3.data.remote.services

import com.example.challenge3.data.remote.responses.PokemonData
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {

    @GET("pokemon/{name}")
    suspend fun listPokemon(@Path("name") name: String?): PokemonData?
}