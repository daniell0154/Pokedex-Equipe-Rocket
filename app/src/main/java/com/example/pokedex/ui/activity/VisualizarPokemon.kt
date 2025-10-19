package com.example.pokedex.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.adapter.TipoAdapter
import com.example.pokedex.api.repository.PokemonRepository
import com.example.pokedex.model.PokemonId
import com.example.pokedex.util.PokemonHelper
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisualizarPokemon : AppCompatActivity() {

    private val repository = PokemonRepository()
    private var mostrandoNormalShiny = false
    private val pokemonHelper = PokemonHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_visualizar_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pokemonId = intent.getIntExtra("POKEMON_ID", -1)
        if (pokemonId != -1) {

            buscarDetalhesDoPokemon(pokemonId)
        }
    }

    fun buscarDetalhesDoPokemon(pokemonId: Int) {

        val textoCor: ShapeableImageView = findViewById(R.id.textoCor)

        repository.getPokemonById(pokemonId).enqueue(object : Callback<PokemonId> {
            override fun onResponse(
                call: Call<PokemonId>,
                response: Response<PokemonId>
            ) {
                if (response.isSuccessful) {
                    val pokemon = response.body()

//                    nome pokemon
                    val nome: TextView = findViewById(R.id.nomePokemonVisu)
                    nome.text = pokemon?.name?.replaceFirstChar { it.uppercaseChar() }

//                    setar imagem
                    val imagem: ShapeableImageView = findViewById(R.id.gifPokemon)
                    Glide.with(this@VisualizarPokemon)
                        .asGif()
                        .load(pokemon?.sprites?.other?.showdown?.front_default)
                        .into(imagem)

//                    setando tipos do pokemon
                    val recyclerTipos: RecyclerView = findViewById(R.id.rvTipos)
                    val tipos = pokemon?.types?.map { it.type.name } ?: emptyList()
                    val tipoAdapter = TipoAdapter(tipos)
                    recyclerTipos.adapter = tipoAdapter
                    recyclerTipos.layoutManager =
                        LinearLayoutManager(
                            this@VisualizarPokemon,
                            LinearLayoutManager.VERTICAL,
                            false
                        )

//                    setando numero
                    val numero: TextView = findViewById(R.id.numeroPokeVisu)
                    numero.text = String.format("Nº %04d", pokemon?.id)

//                    mudando imagem do pokemon
                    textoCor.setOnClickListener {

                        if (mostrandoNormalShiny) {
                            Glide.with(this@VisualizarPokemon)
                                .asGif()
                                .load(pokemon?.sprites?.other?.showdown?.front_default)
                                .into(imagem)
                        } else {
                            Glide.with(this@VisualizarPokemon)
                                .asGif()
                                .load(pokemon?.sprites?.other?.showdown?.front_shiny)
                                .into(imagem)
                        }
                        mostrandoNormalShiny = !mostrandoNormalShiny
                    }

                    findViewById<ImageView>(R.id.btnVoltar).setOnClickListener {
                        finish()
                    }

//                    setando peso
                    val peso: TextView = findViewById(R.id.pesoPokemon)
                    val pesoFinal = pokemon?.weight?.times(0.1)
                    peso.text = "Peso: ${pesoFinal}kg"

//                    setando estatisticas do pokemon
                    val hp: TextView = findViewById(R.id.hp)
                    val ataque: TextView = findViewById(R.id.ataque)
                    val defesa: TextView = findViewById(R.id.defesa)
                    val velocidade: TextView = findViewById(R.id.velocidade)
                    val ataqueEspecial: TextView = findViewById(R.id.ataqueEspecial)
                    val defesaEspecial: TextView = findViewById(R.id.defesaEspecial)

                    hp.text = pokemonHelper.getStats(pokemon!!, "hp")
                    ataque.text = pokemonHelper.getStats(pokemon, "attack")
                    defesa.text = pokemonHelper.getStats(pokemon, "defense")
                    velocidade.text = pokemonHelper.getStats(pokemon, "speed")
                    ataqueEspecial.text = pokemonHelper.getStats(pokemon, "special-attack")
                    defesaEspecial.text = pokemonHelper.getStats(pokemon, "special-defense")
                }
            }

            override fun onFailure(
                call: Call<PokemonId?>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }
        })
    }
}