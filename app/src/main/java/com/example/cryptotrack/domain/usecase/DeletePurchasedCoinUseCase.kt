package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import javax.inject.Inject

class DeletePurchasedCoinUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    suspend operator fun invoke(
        id: Int,
        coinId: String,
        name: String,
        amount: Double,
        buyPrice: Double,
        buyDate: Long,
        imageUrl: String,
    ) {
        return coinRepository.deletePurchasedCoin(
            coin = PurchaseCoin(
                id = id,
                coinId = coinId,
                name = name,
                amount = amount,
                buyPrice = buyPrice,
                buyDate = buyDate,
                imageUrl = imageUrl,
            )
        )
    }

}