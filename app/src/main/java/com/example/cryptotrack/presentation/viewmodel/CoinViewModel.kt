package com.example.cryptotrack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.domain.usecase.DeleteCoinUseCase
import com.example.cryptotrack.domain.usecase.GetCoinsUseCase
import com.example.cryptotrack.domain.usecase.InsertCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinViewModel @Inject constructor(
    private val insertCoinUseCase: InsertCoinUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val deleteCoinUseCase: DeleteCoinUseCase,
) : ViewModel() {


    val coins = getCoinsUseCase()

    fun insertCoin(
        id: String,
        name: String,
    ) {
        viewModelScope.launch {

            insertCoinUseCase(
                RoomCoin(
                    id = id,
                    name = name,
                    path = "",
                )
            )
        }
    }




}