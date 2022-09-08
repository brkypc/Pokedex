package com.example.challenge3.feature.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challenge3.data.local.Pokemon
import com.example.challenge3.R

class PokemonAdapter(private val context: Context, private val pokemons: List<Pokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    var onPokemonClick: ((Pokemon) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mPokemon = pokemons[position]

        holder.pokemonName.text = "#".plus(mPokemon.poke_id).plus(" ").plus(mPokemon.name.replaceFirstChar { it.uppercase() })

        if (mPokemon.imageURL.isNotEmpty())
            Glide.with(context).load(mPokemon.imageURL).into(holder.pokemonImage)
        else
            holder.pokemonImage.setImageResource(R.drawable.poke_ball)
    }

    override fun getItemCount() = pokemons.size

    inner class ViewHolder(_itemView: View) : RecyclerView.ViewHolder(_itemView) {
        val pokemonName: TextView = _itemView.findViewById(R.id.pokemon_name)
        val pokemonImage: ImageView = _itemView.findViewById(R.id.pokemon_image)

        init {
            _itemView.setOnClickListener { onPokemonClick?.invoke(pokemons[adapterPosition]) }
        }
    }
}