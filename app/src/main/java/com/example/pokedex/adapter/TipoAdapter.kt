package com.example.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.mapper.TipoPokemonMapper
import com.google.android.material.imageview.ShapeableImageView
import com.example.pokedex.R

class TipoAdapter(
    private val tipos: List<String>
) : RecyclerView.Adapter<TipoAdapter.TipoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tipo, parent, false)
        return TipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipoViewHolder, position: Int) {
        val tipo = tipos[position]

        val drawable = TipoPokemonMapper.tipoPokemon[tipo] ?: R.drawable.tipo_desconhecido
        holder.bgTipo.setImageResource(drawable)

        holder.nomeTipo.text = tipo.replaceFirstChar { it.uppercaseChar() }
    }

    override fun getItemCount(): Int = tipos.size

    class TipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bgTipo: ShapeableImageView = itemView.findViewById(R.id.bgTipo1)
        val nomeTipo: TextView = itemView.findViewById(R.id.tipo1)
    }
}
