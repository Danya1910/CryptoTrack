package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import javax.inject.Inject

class InsertCoinToHistoryOfViewingUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {

    suspend operator fun invoke(
        coin: HistoryOfViewingCoin,
    ) {
        coinRepository.insertCoinToHistoryOfViewing(coin = coin)
    }
}
