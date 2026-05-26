package com.example.cryptotrack.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.local.entity.CoinEntity


@Database(
    entities = [CoinEntity::class],
    version = 1,
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

}