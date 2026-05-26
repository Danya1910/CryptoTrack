package com.example.cryptotrack.data.local.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptotrack.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetCoin(coin: CoinEntity)

    @Query("SELECT * FROM coins")
    fun getCoins() : Flow<List<CoinEntity>>

    @Query("DELETE FROM coins WHERE id = :id")
    suspend fun deleteCoin(id: String)


}