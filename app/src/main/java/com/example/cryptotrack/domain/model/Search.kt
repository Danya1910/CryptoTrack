package com.example.cryptotrack.domain.model

data class Search(
    val coins: List<SearchCoin>
)


data class SearchCoin(
    val id: String,
    val name: String,
    val apiSymbol: String,
    val symbol: String,
    val marketCapRank: Int?,
    val thumb: String,
    val large: String
)