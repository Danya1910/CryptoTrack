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