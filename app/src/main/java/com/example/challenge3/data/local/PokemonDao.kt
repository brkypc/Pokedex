package com.example.challenge3.data.local

import androidx.room.*

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon LIMIT 20 OFFSET :offset")
    fun getPokemons(offset: Int?): MutableList<Pokemon>

    @Query("SELECT * FROM pokemon WHERE poke_id = :poke_id")
    fun getPokemon(poke_id: Int?): Pokemon

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pokemon: Pokemon)

    @Update
    fun update(pokemon: Pokemon)

    @Query("DELETE FROM pokemon")
    fun deleteAll()
}