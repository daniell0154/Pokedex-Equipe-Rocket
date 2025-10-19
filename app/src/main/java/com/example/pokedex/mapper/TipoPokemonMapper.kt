package com.example.pokedex.mapper

import com.example.pokedex.R

object TipoPokemonMapper {
    val tipoPokemon = mapOf(
        "normal" to R.drawable.tipo_normal,
        "fire" to R.drawable.tipo_fogo,
        "water" to R.drawable.tipo_agua,
        "electric" to R.drawable.tipo_eletrico,
        "grass" to R.drawable.tipo_grama,
        "ice" to R.drawable.tipo_gelo,
        "fighting" to R.drawable.tipo_lutador,
        "poison" to R.drawable.tipo_veneno,
        "ground" to R.drawable.tipo_terra,
        "flying" to R.drawable.tipo_voador,
        "psychic" to R.drawable.tipo_psiquico,
        "bug" to R.drawable.tipo_inseto,
        "rock" to R.drawable.tipo_pedra,
        "ghost" to R.drawable.tipo_fantasma,
        "dragon" to R.drawable.tipo_dragao,
        "dark" to R.drawable.tipo_sombrio,
        "steel" to R.drawable.tipo_aco,
        "fairy" to R.drawable.tipo_fada,
        "stellar" to R.drawable.tipo_estelar
    )

    fun getDrawable(tipo: String): Int {
        return tipoPokemon[tipo] ?: R.drawable.tipo_desconhecido
    }
}