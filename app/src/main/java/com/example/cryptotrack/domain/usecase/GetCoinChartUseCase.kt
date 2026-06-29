package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetCoinChartUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke(
        id: String,
        vsCurrency: String = "usd",
        days: Int = 1
    ) : CoinsChartList {
        return repository.getCoinChart(id = id, vsCurrency = vsCurrency, days = days)
    }

}