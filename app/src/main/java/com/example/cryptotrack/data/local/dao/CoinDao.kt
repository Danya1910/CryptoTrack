package com.example.cryptotrack.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cryptotrack.data.local.entity.CoinEntity
import com.example.cryptotrack.data.local.entity.FavoriteEntity
import com.example.cryptotrack.data.local.entity.PurchaseEntity
import com.example.cryptotrack.data.local.entity.ViewingHistoryEntity
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    // История поиска
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetCoin(coin: CoinEntity)

    @Query("SELECT * FROM coins")
    fun getCoins() : Flow<List<CoinEntity>>

    @Query("DELETE FROM coins WHERE id = :id")
    suspend fun deleteCoin(id: String)

    // История просмотра
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


    // Избранные
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetFavoriteCoin(coin: FavoriteEntity)

    @Query("""
        SELECT * FROM favorites
        ORDER BY timestamp DESC
    """)
    fun getFavoriteCoins() : Flow<List<FavoriteEntity>>

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteCoin(id: String)

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavoriteCoins()


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPurchase(coin: PurchaseEntity)

    @Query("""
        SELECT * FROM purchase
        ORDER BY buyDate DESC
    """)
    fun getPurchasedCoins() : Flow<List<PurchaseEntity>>

    @Delete
    suspend fun deletePurchaseCoin(coin: PurchaseEntity)
}