package com.example.challenge3.di

import android.content.Context
import androidx.room.Room
import com.example.challenge3.data.local.PokemonDao
import com.example.challenge3.data.local.PokemonDatabase
import com.example.challenge3.data.remote.services.PokemonRepoService
import com.example.challenge3.data.remote.services.PokemonService
import com.example.challenge3.utils.Constants.base_url
import com.example.challenge3.utils.Constants.database_name
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HiltModule {
    @Module
    @InstallIn(SingletonComponent::class)
    internal object PokemonModule {

        @Provides
        fun provideDatabase(@ApplicationContext applicationContext: Context): PokemonDatabase {
            return Room.databaseBuilder(
                applicationContext,
                PokemonDatabase::class.java,
                database_name
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun providePokemonDao(database: PokemonDatabase): PokemonDao {
            return database.pokemonDao()
        }

        @Provides
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun providePokemonRepoService(retrofit: Retrofit): PokemonRepoService {
            return retrofit.create(PokemonRepoService::class.java)
        }

        @Provides
        fun providePokemonService(retrofit: Retrofit): PokemonService {
            return retrofit.create(PokemonService::class.java)
        }

        @Provides
        fun provideHashMap(): HashMap<String, Int> {
            val hashMap: HashMap<String, Int> = HashMap()
            hashMap["offset"] = 0
            hashMap["limit"] = 20

            return hashMap
        }

        @Provides
        fun provideExceptionHandler(): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }
        }
    }
}