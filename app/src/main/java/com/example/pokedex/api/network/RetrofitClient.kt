package com.example.pokedex.api.network

import com.example.pokedex.api.network.PokeAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val url = "https://pokeapi.co/api/v2/"

    val instance: PokeAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PokeAPI::class.java)
    }
}