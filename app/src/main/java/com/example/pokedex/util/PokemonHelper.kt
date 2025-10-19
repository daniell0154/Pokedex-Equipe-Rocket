package com.example.pokedex.util

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.example.pokedex.api.repository.PokemonRepository
import com.example.pokedex.model.PokemonId
import com.example.pokedex.model.PokemonResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonHelper {

    private val repository = PokemonRepository()

    fun carregarPokemon(
        limit: Int = 50,
        offset: Int = 0,
        onComplete: (List<PokemonId>) -> Unit
    ) {
        repository.getPokemon(limit = limit, offset = offset).enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val listaPokemon = response.body()?.results ?: emptyList()
                    val listaCompleta: MutableList<PokemonId> = mutableListOf()
                    var carregados = 0

                    for (i in listaPokemon) {
                        val url = i.url
                        val id = url.trimEnd('/').takeLastWhile { it.isDigit() }
                        buscarInfoPoke(id.toInt()) { pokemonDetalhado ->
                            listaCompleta.add(pokemonDetalhado)
                            carregados++
                            if (carregados == listaPokemon.size) {
                                listaCompleta.sortBy { it.id }
                                onComplete(listaCompleta)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                t.printStackTrace()
                onComplete(emptyList())
            }
        })
    }

    fun buscarInfoPoke(id: Int, onResult: (PokemonId) -> Unit) {
        repository.getPokemonById(id).enqueue(object : Callback<PokemonId> {
            override fun onResponse(call: Call<PokemonId>, response: Response<PokemonId>) {
                if (response.isSuccessful) {
                    response.body()?.let { onResult(it) }
                }
            }

            override fun onFailure(call: Call<PokemonId>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getStats(pokemon: PokemonId, statName: String): String {
        return pokemon.stats.find { it.stat.name == statName }?.base_stat?.toString() ?: "0"
    }
}