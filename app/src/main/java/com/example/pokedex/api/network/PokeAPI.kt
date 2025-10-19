package com.example.pokedex.api.network

import com.example.pokedex.model.PokemonId
import com.example.pokedex.model.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeAPI {

    @GET("pokemon")
    fun getPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonResponse>

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: Int): Call<PokemonId>

    @GET("pokemon/{name}")
    fun getPokemonByName(@Path("name") name: String): Call<PokemonId>
}