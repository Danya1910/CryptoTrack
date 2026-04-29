package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.TrendCoins
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetTrendCoinsUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke() : TrendCoins {
        return repository.getTrendCoins()
    }

}