package com.example.pokedex.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pokedex.ui.activity.VisualizarPokemon
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.databinding.FragmentHomeBinding
import com.example.pokedex.model.PokemonId
import com.example.pokedex.util.PokemonHelper
import com.example.pokedex.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val pokemonHelper = PokemonHelper()
    private val listaCompleta = mutableListOf<PokemonId>()
    private val listaFiltrada = mutableListOf<PokemonId>()
    private lateinit var adapter: PokemonAdapter

    private var paginaAtual = 0
    private val limit = 50
    private var isLoading = false
    private val totalPokemon = 1030

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.rvPoke.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        adapter = PokemonAdapter(listaFiltrada) {
            pokemon ->
            val intent = Intent(requireContext(), VisualizarPokemon::class.java)
            intent.putExtra("POKEMON_ID", pokemon.id)
            startActivity(intent)
        }

        binding.pesquisar.addTextChangedListener(object : android.text.TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nome = s.toString().trim().lowercase()
                buscarPokemon(nome)
            }

            override fun afterTextChanged(s: android.text.Editable?) {
            }
        })

        binding.rvPoke.adapter = adapter
        listaFiltrada.addAll(listaCompleta)

        binding.btPassarPoke.setOnClickListener {
            paginaAtual++
            carregarPokemon()
        }

        binding.btVoltarPoke.setOnClickListener {
            if (paginaAtual > 0) {
                paginaAtual--
                carregarPokemon()
            }
        }

        carregarPokemon()

        return binding.root
    }

    private fun carregarPokemon() {
        if (isLoading) return
        isLoading = true
        binding.btPassarPoke.isEnabled = false
        binding.btVoltarPoke.isEnabled = false

        val offset = paginaAtual * limit

        pokemonHelper.carregarPokemon(limit = limit, offset = offset) { lista ->
            listaCompleta.clear()
            listaCompleta.addAll(lista)

            val filtroAtual = binding.pesquisar.text.toString().trim().lowercase()
            if (filtroAtual.isEmpty()) {
                listaFiltrada.clear()
                listaFiltrada.addAll(listaCompleta)
            } else {
                buscarPokemon(filtroAtual)
            }

            adapter.notifyDataSetChanged()
            isLoading = false

            atualizarBotoes()
        }
    }


    private fun buscarPokemon(nome: String) {
        val nomes = nome.split(",")
            .map { it.trim().lowercase() }

        val filtrados = listaCompleta.filter { pokemon ->
            nomes.any { nome -> pokemon.name.contains(nome) }
        }

        listaFiltrada.clear()
        listaFiltrada.addAll(filtrados)

        adapter.notifyDataSetChanged()
    }

    private fun atualizarBotoes() {
        val podeVoltar = paginaAtual > 0
        val podePassar = (paginaAtual + 1) * limit < totalPokemon

        binding.btVoltarPoke.isEnabled = podeVoltar
        binding.btPassarPoke.isEnabled = podePassar

        binding.bgVoltarPoke.setBackgroundResource(if (podeVoltar) R.drawable.botao else R.drawable.botao_desativado)
        binding.bgPassarPoke.setBackgroundResource(if (podePassar) R.drawable.botao else R.drawable.botao_desativado)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
