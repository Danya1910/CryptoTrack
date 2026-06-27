package com.example.cryptotrack.presentation.states

import com.example.cryptotrack.domain.model.FavoriteCoinDetails

data class FavoriteCoinsDetailsState (
    val details: List<FavoriteCoinDetails>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class DetailsOfFoundCoin(
    val details: FavoriteCoinDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)