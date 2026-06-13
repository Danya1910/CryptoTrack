package com.example.cryptotrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "coins")
data class CoinEntity(

    @PrimaryKey
    val id: String,

    val name: String,
    val path: String,
)

@Entity(tableName = "viewing_history")
data class ViewingHistoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val timestamp: Long,
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val timestamp: Long,
)

@Entity(tableName = "purchase")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coinId: String,
    val name: String,
    val amount: Double,
    val buyPrice: Double,
    val buyDate: Long,
    val imageUrl: String,
)
