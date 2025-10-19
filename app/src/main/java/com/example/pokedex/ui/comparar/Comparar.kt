package com.example.pokedex.ui.comparar

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.databinding.FragmentCompararBinding
import com.example.pokedex.model.PokemonId
import com.example.pokedex.util.PokemonHelper
import com.google.android.material.imageview.ShapeableImageView

class Comparar : Fragment() {

    private var binding: FragmentCompararBinding? = null
    private val pokemonHelper = PokemonHelper()
    private val listaCompleta: MutableList<PokemonId> = mutableListOf()
    private var somaStats1Valor = 0
    private var somaStats2Valor = 0

    private val totalPokemon = 1030
    private val limit = 50

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompararBinding.inflate(inflater, container, false)

        binding?.selecao?.setOnClickListener { mostrarPopup { pokemon -> exibirPokemon(pokemon, 1) } }
        binding?.selecao2?.setOnClickListener { mostrarPopup { pokemon -> exibirPokemon(pokemon, 2) } }

        binding?.resultado?.setOnClickListener {
            val resultado = when {
                somaStats1Valor > somaStats2Valor -> "O pokémon mais forte é o ${binding!!.nomePokemon1.text}!"
                somaStats2Valor > somaStats1Valor -> "O pokémon mais forte é o ${binding!!.nomePokemon2.text}!"
                else -> "Empate! Ambos são igualmente fortes."
            }
            binding!!.resultadotxt.text = resultado
        }

        return binding!!.root
    }

    private fun mostrarPopup(onPokemonSelected: (PokemonId) -> Unit) {
        val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
        val recycler = popupView.findViewById<RecyclerView>(R.id.recyclerPopup)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val btPassar: Button = popupView.findViewById(R.id.btPassarPoke)
        val btVoltar: Button = popupView.findViewById(R.id.btVoltarPoke)
        val bgPassar: ShapeableImageView = popupView.findViewById(R.id.bgPassarPoke)
        val bgVoltar: ShapeableImageView = popupView.findViewById(R.id.bgVoltarPoke)

        var paginaPopup = 0
        val limitPopup = limit
        val totalPopup = totalPokemon

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(popupView)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        fun carregarPagina() {
            val offset = paginaPopup * limitPopup
            pokemonHelper.carregarPokemon(limit = limitPopup, offset = offset) { lista ->
                listaCompleta.clear()
                listaCompleta.addAll(lista)
                recycler.adapter = PokemonAdapter(listaCompleta) { pokemon ->
                    onPokemonSelected(pokemon)
                    dialog.dismiss()
                }

                // atualizar botões
                val podeVoltar = paginaPopup > 0
                val podePassar = (paginaPopup + 1) * limitPopup < totalPopup

                btVoltar.isEnabled = podeVoltar
                btPassar.isEnabled = podePassar
                bgVoltar.setBackgroundResource(if (podeVoltar) R.drawable.botao else R.drawable.botao_desativado)
                bgPassar.setBackgroundResource(if (podePassar) R.drawable.botao else R.drawable.botao_desativado)
            }
        }

        btPassar.setOnClickListener {
            if ((paginaPopup + 1) * limitPopup < totalPopup) {
                paginaPopup++
                carregarPagina()
            }
        }

        btVoltar.setOnClickListener {
            if (paginaPopup > 0) {
                paginaPopup--
                carregarPagina()
            }
        }

        carregarPagina()
        dialog.show()
    }

    private fun exibirPokemon(pokemon: PokemonId, slot: Int) {
        val nomeId = if (slot == 1) R.id.nomePokemon1 else R.id.nomePokemon2
        val imgId = if (slot == 1) R.id.imgPokemon1 else R.id.imgPokemon2
        val container = if (slot == 1) binding!!.pokemon1Container else binding!!.pokemon2Container
        val selecao = if (slot == 1) binding!!.selecao else binding!!.selecao2

        val nome: TextView = requireView().findViewById(nomeId)
        nome.text = pokemon.name.replaceFirstChar { it.uppercaseChar() }

        val imagem: ShapeableImageView = requireView().findViewById(imgId)
        Glide.with(this)
            .asGif()
            .load(pokemon.sprites.other.showdown.front_default)
            .into(imagem)

        container.visibility = View.VISIBLE
        selecao.visibility = View.GONE
        if (slot == 1) binding!!.bg.visibility = View.GONE else binding!!.bg2.visibility = View.GONE

        // Preenche stats
        val hp = pokemonHelper.getStats(pokemon, "hp").toInt()
        val ataque = pokemonHelper.getStats(pokemon, "attack").toInt()
        val defesa = pokemonHelper.getStats(pokemon, "defense").toInt()
        val velocidade = pokemonHelper.getStats(pokemon, "speed").toInt()
        val ataqueEspecial = pokemonHelper.getStats(pokemon, "special-attack").toInt()
        val defesaEspecial = pokemonHelper.getStats(pokemon, "special-defense").toInt()
        val somaStats = hp + ataque + defesa + velocidade + ataqueEspecial + defesaEspecial

        val stats: TextView = requireView().findViewById(
            resources.getIdentifier("somaStats$slot", "id", requireContext().packageName)
        )
        stats.text = "Soma das estatísticas: $somaStats"

        if (slot == 1) somaStats1Valor = somaStats else somaStats2Valor = somaStats

        if (binding?.pokemon1Container?.visibility == View.VISIBLE && binding?.pokemon2Container?.visibility == View.VISIBLE) {
            binding?.resultado?.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}