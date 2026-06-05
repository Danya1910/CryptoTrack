package com.example.cryptotrack.presentation.util.uiModels

import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.FavoriteCoinDetails


sealed class FavoriteUiItem {
    data class Full(
        val data: FavoriteCoinDetails
    ) : FavoriteUiItem()

    data class Basic(
        val data: FavoriteCoin
    ) : FavoriteUiItem()
}