package com.example.cryptotrack.domain.model

data class PurchaseCoin(
    val id: Int = 0,
    val coinId: String,
    val name: String,
    val amount: Double,
    val buyPrice: Double,
    val buyDate: Long,
)