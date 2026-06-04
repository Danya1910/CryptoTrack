package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.repository.CoinRepository
import javax.inject.Inject

class DeleteAllFavoriteCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    suspend operator fun invoke() {
        coinRepository.deleteAllFavoriteCoins()
    }

}