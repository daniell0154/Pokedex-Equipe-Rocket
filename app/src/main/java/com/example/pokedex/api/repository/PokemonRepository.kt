package com.example.pokedex.api.repository

import com.example.pokedex.api.network.RetrofitClient
import com.example.pokedex.model.PokemonId

class PokemonRepository {

    fun getPokemon(limit: Int, offset: Int) =
        RetrofitClient.instance.getPokemon(limit, offset)

    fun getPokemonById(id: Int) = RetrofitClient.instance.getPokemonById(id)

    fun getPokemonByName(name: String) = RetrofitClient.instance.getPokemonByName(name)
}