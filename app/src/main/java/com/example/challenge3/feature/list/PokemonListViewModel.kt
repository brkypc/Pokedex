package com.example.challenge3.feature.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challenge3.PokemonApplication
import com.example.challenge3.data.local.Pokemon
import com.example.challenge3.data.local.PokemonDao
import com.example.challenge3.data.remote.responses.Result
import com.example.challenge3.data.remote.services.PokemonRepoService
import com.example.challenge3.data.remote.services.PokemonService
import com.example.challenge3.utils.Constants
import com.example.challenge3.utils.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    app: Application,
    private val database: PokemonDao,
    private val repoService: PokemonRepoService,
    private val service: PokemonService,
    private var hashMap: HashMap<String, Int>,
    private val coroutineExceptionHandler: CoroutineExceptionHandler
) : AndroidViewModel(app) {
    val pokemonLiveData = MutableLiveData<ArrayList<Pokemon>>()
    val internetStatusLiveData = MutableLiveData<Int>()

    fun getPokemonsFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val pokemons = database.getPokemons(hashMap["offset"])

            if (pokemons.size != 0) pokemonLiveData.postValue(pokemons as ArrayList<Pokemon>)
            else getPokemonsFromInternet()
        }
    }

    private fun fetchPokemonsAndSave(pokemons: ArrayList<Pokemon>) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            pokemons.forEach { pokemon ->
                database.insert(pokemon)
            }
            pokemons.forEach { pokemon ->
                service.listPokemon(pokemon.name)?.let {
                    pokemon.apply {
                        imageURL = it.sprites.other?.home?.frontDefault ?: ""
                        height = it.height / 10F
                        weight = it.weight / 10F
                        abilities = it.abilities
                        stats = it.stats
                        types = it.types
                    }
                    database.update(pokemon)
                }
            }
        }
    }

    fun getPokemonsFromInternet() {
        if (hasInternetConnection()) {
            internetStatusLiveData.postValue(1) //  1 -> loading 2-> no internet
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                repoService.listRepo(hashMap)?.let {
                    createPokemons(it.results)
                }
            }
        } else {
            internetStatusLiveData.postValue(2)
            hashMap["offset"]?.let {
                if (it != 0) hashMap["offset"] = hashMap["offset"]!!.minus(20)
            }
        }
    }

    private fun createPokemons(results: List<Result>) {
        val pokemons: ArrayList<Pokemon> = arrayListOf()

        for (result in results) {

            result.url?.let {
                val pokeId = it.split("/", ignoreCase = false, limit = 0)[6].toInt()
                val imageURL = Constants.baseImageURL.plus(pokeId).plus(".png")

                pokemons.add(Pokemon(pokeId, result.name ?: "pokemon", imageURL))
            }
        }
        pokemonLiveData.postValue(pokemons)
        fetchPokemonsAndSave(pokemons)
    }

    fun onPageClick(number : Int) {
        hashMap["offset"]?.let {
            hashMap["offset"] = (number-1) * 20

            getPokemonsFromDatabase()
        }
    }

    fun onPreviousClick() {
        hashMap["offset"]?.let {
            hashMap["offset"] = hashMap["offset"]!!.minus(20)

            getPokemonsFromDatabase()
        }
    }

    fun onNextClick() {
        hashMap["offset"]?.let {
            hashMap["offset"] = hashMap["offset"]!!.plus(20)

            getPokemonsFromDatabase()
        }
    }

    private fun hasInternetConnection() =
        NetworkUtil.isConnected(getApplication<PokemonApplication>().applicationContext)
}