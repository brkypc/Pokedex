package com.example.challenge3.data.local

import androidx.room.*

@Database(entities = [Pokemon::class], version = 2)
@TypeConverters(DataConverter::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}