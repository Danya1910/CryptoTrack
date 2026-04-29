package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class SearchCoinsUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke(
        query: String
    ) : Search {
        return repository.searchCoins(query = query)
    }

}