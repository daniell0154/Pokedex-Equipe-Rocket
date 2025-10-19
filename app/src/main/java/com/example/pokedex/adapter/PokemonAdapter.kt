package com.example.pokedex.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.example.pokedex.R
import com.example.pokedex.mapper.TipoPokemonMapper
import com.example.pokedex.model.PokemonId
import com.example.pokedex.util.PokemonHelper

class PokemonAdapter(
    private val pokemonLista: List<PokemonId>,
    private val onItemClick: (PokemonId) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private val pokemonHelper: PokemonHelper = PokemonHelper()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonAdapter.PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }


    override fun onBindViewHolder(holder: PokemonAdapter.PokemonViewHolder, position: Int) {
        val pokemon = pokemonLista[position]


//        id/numero
        val numeroFormatado = String.format("Nº %04d", pokemonLista[position].id)
        holder.numeroPoke.text = numeroFormatado

//        nome
        holder.nomePokemon.text = pokemon.name.replaceFirstChar { it.uppercaseChar() }


//        imagem pokemon
        if (!pokemonLista.getOrNull(position)?.sprites?.other?.official_artwork?.front_default.isNullOrEmpty()) {
            Glide.with(holder.imgPokemonFrente.context)
                .load(pokemonLista[position].sprites.other?.official_artwork?.front_default)
                .circleCrop()
                .into(holder.imgPokemonFrente)
        } else {
            holder.imgPokemonFrente.setImageResource(R.drawable.img_placeholder)
        }

//       mudando pra tela e mostrar um pokemon só
        holder.itemView.setOnClickListener {
            onItemClick(pokemonLista[position])
        }

    }

    override fun getItemCount(): Int {
        return pokemonLista.size
    }

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPokemonFrente: ShapeableImageView = itemView.findViewById(R.id.imgPokemonFrente)
        val nomePokemon: TextView = itemView.findViewById(R.id.nomePokemon)
        val numeroPoke: TextView = itemView.findViewById(R.id.numeroPokemon)
    }
}