package com.example.cryptotrack.data.local.dao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cryptotrack.data.local.entity.CoinEntity
import com.example.cryptotrack.data.local.entity.ViewingHistoryEntity
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetCoin(coin: CoinEntity)

    @Query("SELECT * FROM coins")
    fun getCoins() : Flow<List<CoinEntity>>

    @Query("DELETE FROM coins WHERE id = :id")
    suspend fun deleteCoin(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinToHistoryOfViewing(coin: ViewingHistoryEntity)

    @Query("SELECT * FROM viewing_history")
    fun getCoinsFromHistoryOfViewing() : Flow<List<ViewingHistoryEntity>>

    @Query("""
DELETE FROM viewing_history
WHERE id IN (
    SELECT id FROM viewing_history
    ORDER BY timestamp DESC
    LIMIT -1 OFFSET 15
)
""")
    suspend fun trimHistory()

    @Transaction
    suspend fun insertHistoryWithLimit(coin: ViewingHistoryEntity) {
        insertCoinToHistoryOfViewing(coin)
        trimHistory()
    }

}