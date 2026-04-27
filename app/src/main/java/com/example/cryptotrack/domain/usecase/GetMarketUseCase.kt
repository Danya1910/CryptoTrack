package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetMarketUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke() : List<MarketData> {
        return repository.getMarket()
    }

}