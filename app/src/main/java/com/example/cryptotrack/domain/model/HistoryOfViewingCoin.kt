package com.example.cryptotrack.domain.model

data class HistoryOfViewingCoin (
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val timestamp: Long,
)
