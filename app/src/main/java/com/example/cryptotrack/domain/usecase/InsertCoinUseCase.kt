package com.example.cryptotrack.domain.usecase

import androidx.compose.runtime.referentialEqualityPolicy
import com.example.cryptotrack.domain.repository.CoinRepository
import com.example.cryptotrack.domain.model.RoomCoin
import javax.inject.Inject

class InsertCoinUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    suspend operator fun invoke(coin: RoomCoin) {
        coinRepository.insertCoin(coin = coin)
    }

}