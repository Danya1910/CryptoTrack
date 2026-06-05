package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetFavoriteCoinsDetailsUseCase @Inject constructor(
    private val repository: CoinGeckoRepository,
) {

    suspend operator fun invoke(
        ids: String
    ): List<FavoriteCoinDetails> {
        return repository.getFavoriteCoinsDetails(ids = ids)
    }

}