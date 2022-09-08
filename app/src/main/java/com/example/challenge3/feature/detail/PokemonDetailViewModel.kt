package com.example.challenge3.feature.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challenge3.PokemonApplication
import com.example.challenge3.data.local.Pokemon
import com.example.challenge3.data.local.PokemonDao
import com.example.challenge3.data.remote.services.PokemonService
import com.example.challenge3.utils.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    app: Application,
    private val database: PokemonDao,
    private val service: PokemonService,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : AndroidViewModel(app) {
    val pokemonLiveData = MutableLiveData<Pokemon>()
    val internetStatusLiveData = MutableLiveData<Int>()

    fun getPokemonFromDatabase(pokeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pokemon = database.getPokemon(pokeId)
            if (pokemon.height != 0F) pokemonLiveData.postValue(pokemon)
            else getPokemonFromInternet(pokemon)
        }
    }

    private fun getPokemonFromInternet(pokemon: Pokemon) {
        if (hasInternetConnection()) {
            internetStatusLiveData.postValue(1)
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                service.listPokemon(pokemon.name)?.let {
                    pokemon.apply {
                        imageURL = it.sprites.other?.home?.frontDefault ?: ""
                        height = it.height / 10F
                        weight = it.weight / 10F
                        abilities = it.abilities
                        stats = it.stats
                        types = it.types
                    }
                    pokemonLiveData.postValue(pokemon)

                    database.update(pokemon)
                }
            }
        } else internetStatusLiveData.postValue(2)
    }

    private fun hasInternetConnection() =
        NetworkUtil.isConnected(getApplication<PokemonApplication>().applicationContext)
}