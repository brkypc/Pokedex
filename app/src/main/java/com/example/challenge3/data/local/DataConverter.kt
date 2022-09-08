package com.example.challenge3.data.local

import androidx.room.TypeConverter
import com.example.challenge3.data.remote.responses.AbilityParent
import com.example.challenge3.data.remote.responses.StatParent
import com.example.challenge3.data.remote.responses.TypeParent

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    @TypeConverter
    fun fromAbilitiesList(abilities: List<AbilityParent?>?): String? {
        if (abilities == null) return null
        val type = object : TypeToken<List<AbilityParent?>?>() {}.type
        return Gson().toJson(abilities, type)
    }

    @TypeConverter
    fun toAbilitiesList(abilitiesString: String?): List<AbilityParent>? {
        if (abilitiesString == null) return null
        val type = object : TypeToken<List<AbilityParent?>?>() {}.type
        return Gson().fromJson<List<AbilityParent>>(abilitiesString, type)
    }

    @TypeConverter
    fun fromStatsList(stats: List<StatParent?>?): String? {
        if (stats == null) return null
        val type = object : TypeToken<List<StatParent?>?>() {}.type
        return Gson().toJson(stats, type)
    }

    @TypeConverter
    fun toStatsList(statsString: String?): List<StatParent>? {
        if (statsString == null) return null
        val type = object : TypeToken<List<StatParent?>?>() {}.type
        return Gson().fromJson<List<StatParent>>(statsString, type)
    }

    @TypeConverter
    fun fromTypesList(types: List<TypeParent?>?): String? {
        if (types == null) return null
        val type = object : TypeToken<List<TypeParent?>?>() {}.type
        return Gson().toJson(types, type)
    }

    @TypeConverter
    fun toTypesList(typesString: String?): List<TypeParent>? {
        if (typesString == null) return null
        val type = object : TypeToken<List<TypeParent?>?>() {}.type
        return Gson().fromJson<List<TypeParent>>(typesString, type)
    }
}