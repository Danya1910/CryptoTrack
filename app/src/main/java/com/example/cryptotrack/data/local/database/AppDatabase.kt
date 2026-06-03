package com.example.cryptotrack.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.local.entity.CoinEntity
import com.example.cryptotrack.data.local.entity.FavoriteEntity
import com.example.cryptotrack.data.local.entity.ViewingHistoryEntity


@Database(
    entities = [
        CoinEntity::class,
        ViewingHistoryEntity::class,
        FavoriteEntity:: class
    ],
    version = 3,
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
    CREATE TABLE viewing_history (
        id TEXT PRIMARY KEY NOT NULL,
        name TEXT NOT NULL,
        symbol TEXT NOT NULL,
        imageUrl TEXT NOT NULL,
        timestamp INTEGER NOT NULL
    )
""".trimIndent()
        )

    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE favorites (
                id TEXT NOT NULL,
                name TEXT NOT NULL,
                symbol TEXT NOT NULL,
                imageUrl TEXT NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )
    }
}