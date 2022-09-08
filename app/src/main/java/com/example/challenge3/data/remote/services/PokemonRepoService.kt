package com.example.challenge3.data.remote.services

import com.example.challenge3.data.remote.responses.PokemonRepo
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PokemonRepoService {

    @GET("pokemon/")
    suspend fun listRepo(@QueryMap name: HashMap<String, Int>): PokemonRepo?
}