package com.example.cryptotrack.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.domain.usecase.DeleteCoinUseCase
import com.example.cryptotrack.domain.usecase.DeleteFavoriteCoinUseCase
import com.example.cryptotrack.domain.usecase.GetCoinsFromHistoryOfViewingUseCase
import com.example.cryptotrack.domain.usecase.GetCoinsUseCase
import com.example.cryptotrack.domain.usecase.GetFavoriteCoinsUseCase
import com.example.cryptotrack.domain.usecase.InsertCoinToHistoryOfViewingUseCase
import com.example.cryptotrack.domain.usecase.InsertCoinUseCase
import com.example.cryptotrack.domain.usecase.InsertFavoriteCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinViewModel @Inject constructor(
    private val insertCoinUseCase:InsertCoinUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val deleteCoinUseCase: DeleteCoinUseCase,
    private val insertCoinToHistoryOfViewingUseCase: InsertCoinToHistoryOfViewingUseCase,
    private val getCoinsFromHistoryOfViewingUseCase: GetCoinsFromHistoryOfViewingUseCase,
    private val insertFavoriteCoinUseCase: InsertFavoriteCoinUseCase,
    private val getFavoriteCoinsUseCase: GetFavoriteCoinsUseCase,
    private val deleteFavoriteCoinUseCase: DeleteFavoriteCoinUseCase,
) : ViewModel() {


    val coins = getCoinsUseCase()

    val historyOfViewingCoins = getCoinsFromHistoryOfViewingUseCase()

    val favoriteCoins = getFavoriteCoinsUseCase()

    init {

        viewModelScope.launch {

            coins.collect { list ->

                Log.d("CoinVM", "coins = $list")
            }

            historyOfViewingCoins.collect { list->
                Log.d("CoinVM", "historyCoins = $list")
            }

            favoriteCoins.collect { list->
                Log.d("CoinVM", "favoriteCoins = $list")
            }
        }
    }

    fun insertCoin(
        id: String,
        name: String,
        path: String,
    ) {
        viewModelScope.launch {

            insertCoinUseCase(
                RoomCoin(
                    id = id,
                    name = name,
                    path = path,
                )
            )
            Log.d("CoinVM", "insertCoin called")
        }
    }


    fun insertCoinHistoryOfViewing(
        id: String,
        name:String,
        symbol:String,
        imageUrl: String,
        timestamp: Long
    ) {
        viewModelScope.launch {
            insertCoinToHistoryOfViewingUseCase(
                HistoryOfViewingCoin(
                    id = id,
                    name = name,
                    symbol = symbol,
                    imageUrl = imageUrl,
                    timestamp = timestamp,
                )
            )
            Log.d("CoinVM", "insertCoinHistoryOfViewing")
        }
    }

    fun insertFavoriteCoin(
        id: String,
        name:String,
        symbol:String,
        imageUrl: String,
    ) {
        viewModelScope.launch {
            insertFavoriteCoin(
                id = id,
                name = name,
                symbol = symbol,
                imageUrl = imageUrl,
            )
            Log.d("CoinVM", "insertFavoriteCoin")
        }
    }

    fun deleteCoin(
        id: String
    ) {
        viewModelScope.launch {
            deleteCoinUseCase(id = id)
            Log.d("CoinVM", "deleteCoin called")
        }
    }

    fun deleteFavoriteCoin(
        id: String
    ) {
        viewModelScope.launch {
            deleteFavoriteCoin(id = id)
            Log.d("CoinVM", "deleteFavoriteCoin called")
        }
    }
}