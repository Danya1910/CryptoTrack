package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.repository.CoinRepository
import javax.inject.Inject

class DeleteFavoriteCoinUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    suspend operator fun invoke(
        id: String
    ) {
        coinRepository.deleteFavoriteCoin(id = id)
    }

}