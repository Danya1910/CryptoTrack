package com.example.cryptotrack.presentation.util.uiModels

data class AggregatedPurchase(
    val coinId: String,
    val name: String,
    val totalAmount: Double,
    val totalValue: Double,
    val imageUrl: String,
)