package com.example.challenge3.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.challenge3.data.remote.responses.AbilityParent
import com.example.challenge3.data.remote.responses.StatParent
import com.example.challenge3.data.remote.responses.TypeParent

@Entity
data class Pokemon (
    @PrimaryKey val poke_id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") var imageURL: String = "",
    @ColumnInfo(name = "height") var height: Float? = 0F,
    @ColumnInfo(name = "weight") var weight: Float? = 0F,
    @ColumnInfo(name = "abilities") var abilities: List<AbilityParent>? = null,
    @ColumnInfo(name = "stats") var stats: List<StatParent>? = null,
    @ColumnInfo(name = "types") var types: List<TypeParent>? = null
)