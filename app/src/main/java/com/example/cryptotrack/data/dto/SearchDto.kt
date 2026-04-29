package com.example.cryptotrack.data.dto


data class SearchDto(
    val coins: List<SearchCoinDto>
)


data class SearchCoinDto(
    val id: String,
    val name: String,
    val api_symbol: String,
    val symbol: String,
    val market_cap_rank: Int?,
    val thumb: String,
    val large: String
)
