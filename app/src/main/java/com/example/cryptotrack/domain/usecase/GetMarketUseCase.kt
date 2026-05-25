package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import com.example.cryptotrack.domain.util.MarketOrder
import javax.inject.Inject

class GetMarketUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke(
        order: MarketOrder = MarketOrder.DEFAULT
    ) : List<MarketData> {
        return repository.getMarket(order = order)
    }

}