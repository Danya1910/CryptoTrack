package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    operator fun invoke() : Flow<List<FavoriteCoin>> {
        return coinRepository.getFavoriteCoins()
    }
}