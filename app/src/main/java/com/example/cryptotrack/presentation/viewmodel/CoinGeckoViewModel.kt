package com.example.cryptotrack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.SearchCoin
import com.example.cryptotrack.domain.usecase.GetCoinChartUseCase
import com.example.cryptotrack.domain.usecase.GetCoinDetailsUseCase
import com.example.cryptotrack.domain.usecase.GetGlobalMarketUseCase
import com.example.cryptotrack.domain.usecase.GetMarketUseCase
import com.example.cryptotrack.domain.usecase.GetTrendCoinsUseCase
import com.example.cryptotrack.domain.usecase.SearchCoinsUseCase
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.states.DetailsScreenStates
import com.example.cryptotrack.presentation.states.GlobalMarketState
import com.example.cryptotrack.presentation.states.MarketDataState
import com.example.cryptotrack.presentation.states.SearchState
import com.example.cryptotrack.presentation.states.TrendState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject


@HiltViewModel
class CoinGeckoViewModel @Inject constructor(
    private val getGlobalMarketUseCase: GetGlobalMarketUseCase,
    private val getMarketUseCase: GetMarketUseCase,
    private val getTrendCoinsUseCase: GetTrendCoinsUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
    private val searchCoinsUseCase: SearchCoinsUseCase,
) : ViewModel() {

    private val _order = MutableStateFlow(MarketOrder.DEFAULT)
    var order = _order.asStateFlow()

    private val _globalMarketState = MutableStateFlow(GlobalMarketState())
    private val _marketDataState = MutableStateFlow(MarketDataState())
    private val _detailScreenState = MutableStateFlow(DetailsScreenStates())
    private val _trendState = MutableStateFlow(TrendState())
    private val _searchState = MutableStateFlow(SearchState())
    val marketScreenState = _globalMarketState.asStateFlow()
    val marketDataState = _marketDataState.asStateFlow()
    val detailsScreenState = _detailScreenState.asStateFlow()
    val trendState = _trendState.asStateFlow()
    val searchState = _searchState.asStateFlow()


    init {
        loadMarket(order = MarketOrder.DEFAULT)
    }

    fun loadGlobalMarket() {
        viewModelScope.launch {
            _globalMarketState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                coroutineScope {

                    getGlobalMarketUseCase()

                }
            }.onSuccess { globalMarket ->
                _globalMarketState.update {
                    it.copy(
                        globalMarket = globalMarket,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->

                if (throwable is CancellationException) {
                    throw throwable
                }

                _globalMarketState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun loadTrends() {
        viewModelScope.launch {
            _trendState.update {
                it.copy(
                    isLoading = true
                )
            }
            runCatching {
                getTrendCoinsUseCase()
            }.onSuccess { trend->
                _trendState.update {
                    it.copy(
                        isLoading = false,
                        trendCoins = trend
                    )
                }
            }.onFailure { throwable ->
                if(throwable is CancellationException) {
                    throw throwable
                }
                _trendState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun loadDetails(
        coinId: String
    ) {
        viewModelScope.launch {
            _detailScreenState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                coroutineScope {
                    val details = async {
                        getCoinDetailsUseCase(id = coinId)
                    }
                    val chart = async {
                        getCoinChartUseCase(id = coinId)
                    }
                    Pair(
                        first = details.await(),
                        second = chart.await()
                    )
                }
            }.onSuccess { (details, chart) ->
                _detailScreenState.update {
                    it.copy(
                        details = details,
                        chart = chart,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _detailScreenState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun changeOrder(
        order: MarketOrder,
    ) {
        _order.value = order

        loadMarket(order = order)
    }

    fun loadMarket(
        order: MarketOrder
    ) {
        viewModelScope.launch {
            _marketDataState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                getMarketUseCase(order)
            }.onSuccess { market ->
                _marketDataState.update {
                    it.copy(
                        market = market,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }

                _marketDataState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun search(
        query: String
    ) {
        viewModelScope.launch {
            _searchState.update {
                it.copy(
                    isLoading = true
                )
            }
            runCatching {
                searchCoinsUseCase(query = query)
            }.onSuccess { suggestions->
                _searchState.update {
                    it.copy(
                        isLoading = false,
                        suggestions = suggestions,
                    )
                }
            }.onFailure { throwable ->
                if(throwable is CancellationException){
                    throw throwable
                }
                _searchState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message,
                    )
                }
            }
        }
    }

    fun clearSuggestionsList() {
        _searchState.update {
            it.copy(
                suggestions = Search(
                    coins = emptyList()
                )
            )
        }
    }

}