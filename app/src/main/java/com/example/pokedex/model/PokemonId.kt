package com.example.pokedex.model

data class PokemonId(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<Stats>,
    val weight: Int,
    val species: Species
)