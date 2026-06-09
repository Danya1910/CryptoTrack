package com.example.cryptotrack.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.local.dao.UserDao
import com.example.cryptotrack.data.local.entity.CoinEntity
import com.example.cryptotrack.data.local.entity.FavoriteEntity
import com.example.cryptotrack.data.local.entity.PurchaseEntity
import com.example.cryptotrack.data.local.entity.UserEntity
import com.example.cryptotrack.data.local.entity.ViewingHistoryEntity


@Database(
    entities = [
        CoinEntity::class,
        ViewingHistoryEntity::class,
        FavoriteEntity::class,
        PurchaseEntity::class,
        UserEntity::class,
    ],
    version = 6,
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    abstract fun userDao(): UserDao

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

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            ALTER TABLE favorites ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0
        """.trimIndent()
        )

    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE purchase (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                coinId TEXT NOT NULL,
                name TEXT NOT NULL,
                amount REAL NOT NULL,
                buyPrice REAL NOT NULL,
                buyDate INTEGER NOT NULL
            )
            """.trimIndent()
        )

    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE user (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                avatar TEXT
            )
            """.trimIndent()
        )

    }
}