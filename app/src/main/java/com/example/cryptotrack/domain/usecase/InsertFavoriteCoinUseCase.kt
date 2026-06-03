package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import javax.inject.Inject

class InsertFavoriteCoinUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {

    suspend operator fun invoke(
        coin: FavoriteCoin
    ) {
        coinRepository.insertFavoriteCoin(coin = coin)
    }

}