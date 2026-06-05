package com.example.cryptotrack.presentation.states

import com.example.cryptotrack.domain.model.FavoriteCoinDetails

data class FavoriteCoinsDetailsState (
    val details: List<FavoriteCoinDetails>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)