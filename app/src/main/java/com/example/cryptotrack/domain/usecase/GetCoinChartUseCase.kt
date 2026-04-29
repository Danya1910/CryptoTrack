package com.example.cryptotrack.domain.usecase

import android.icu.util.Currency
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetCoinChartUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke(
        id: String,
        vsCurrency: String,
        days: Int
    ) : CoinsChartList {
        return repository.getCoinChart(id = id, vsCurrency = vsCurrency, days = days)
    }

}