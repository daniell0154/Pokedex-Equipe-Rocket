package com.example.pokedex.model

import com.google.gson.annotations.SerializedName

data class Other(
    @SerializedName("official-artwork")
    val official_artwork: OfficialArtwork,
    val showdown: Showdown
)
