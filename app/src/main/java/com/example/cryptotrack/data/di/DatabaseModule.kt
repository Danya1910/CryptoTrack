package com.example.cryptotrack.data.di

import android.content.Context
import androidx.room.Room
import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.local.database.AppDatabase
import com.example.cryptotrack.data.local.database.MIGRATION_1_2
import com.example.cryptotrack.data.local.database.MIGRATION_2_3
import com.example.cryptotrack.data.local.database.MIGRATION_3_4
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "crypto_db"
        )
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
            )
            .build()
    }

    @Provides
    fun provideCoinDao(
        database: AppDatabase
    ): CoinDao {
        return database.coinDao()
    }
}