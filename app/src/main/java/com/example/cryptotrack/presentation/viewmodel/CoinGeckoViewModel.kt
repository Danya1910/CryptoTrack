package com.example.cryptotrack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.usecase.GetCoinChartUseCase
import com.example.cryptotrack.domain.usecase.GetCoinDetailsUseCase
import com.example.cryptotrack.domain.usecase.GetFavoriteCoinsDetailsUseCase
import com.example.cryptotrack.domain.usecase.GetGlobalMarketUseCase
import com.example.cryptotrack.domain.usecase.GetMarketUseCase
import com.example.cryptotrack.domain.usecase.GetTrendCoinsUseCase
import com.example.cryptotrack.domain.usecase.SearchCoinsUseCase
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.states.ChartState
import com.example.cryptotrack.presentation.states.DetailsState
import com.example.cryptotrack.presentation.states.FavoriteCoinsDetailsState
import com.example.cryptotrack.presentation.states.GlobalMarketState
import com.example.cryptotrack.presentation.states.MarketDataState
import com.example.cryptotrack.presentation.states.SearchState
import com.example.cryptotrack.presentation.states.TrendState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinGeckoViewModel @Inject constructor(
    private val getGlobalMarketUseCase: GetGlobalMarketUseCase,
    private val getMarketUseCase: GetMarketUseCase,
    private val getTrendCoinsUseCase: GetTrendCoinsUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
    private val searchCoinsUseCase: SearchCoinsUseCase,
    private val getFavoriteCoinsDetailsUseCase: GetFavoriteCoinsDetailsUseCase,
) : ViewModel() {

    private val _order = MutableStateFlow(MarketOrder.DEFAULT)
    var order = _order.asStateFlow()

    private val _globalMarketState = MutableStateFlow(GlobalMarketState())
    private val _marketDataState = MutableStateFlow(MarketDataState())
    private val _detailsState = MutableStateFlow(DetailsState())
    private val _chartState = MutableStateFlow(ChartState())
    private val _trendState = MutableStateFlow(TrendState())
    private val _searchState = MutableStateFlow(SearchState())
    private val _favoriteCoinsDetailsState = MutableStateFlow(FavoriteCoinsDetailsState())
    val marketScreenState = _globalMarketState.asStateFlow()
    val marketDataState = _marketDataState.asStateFlow()
    val detailsState = _detailsState.asStateFlow()
    val chartState = _chartState.asStateFlow()
    val trendState = _trendState.asStateFlow()
    val searchState = _searchState.asStateFlow()
    val favoriteCoinsDetailsState = _favoriteCoinsDetailsState.asStateFlow()


    init {
        loadMarket(order = MarketOrder.DEFAULT)
    }

    fun loadGlobalMarket() {
        if (_globalMarketState.value.globalMarket != null || _globalMarketState.value.isLoading) return
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
        if(_trendState.value.trendCoins != null || _trendState.value.isLoading) return
        viewModelScope.launch {
            _trendState.update {
                it.copy(
                    isLoading = true
                )
            }
            runCatching {
                getTrendCoinsUseCase()
            }.onSuccess { trend ->
                _trendState.update {
                    it.copy(
                        isLoading = false,
                        trendCoins = trend
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
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
        coinId: String,
    ) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    details = null,
                )
            }
            runCatching {
                coroutineScope {
                    getCoinDetailsUseCase(id = coinId)
                }
            }.onSuccess { details ->
                _detailsState.update {
                    it.copy(
                        details = details,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _detailsState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun loadChart(
        coinId: String,
        days: Int = 1,
    ) {
        viewModelScope.launch {
            _chartState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    chart = null
                )
            }
            runCatching {
                coroutineScope {
                    getCoinChartUseCase(id = coinId,days = days)
                }
            }.onSuccess { chart ->
                _chartState.update {
                    it.copy(
                        chart = chart,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _chartState.update {
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
        if(!_marketDataState.value.market.isNullOrEmpty() || _marketDataState.value.isLoading) return
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
            }.onSuccess { suggestions ->
                _searchState.update {
                    it.copy(
                        isLoading = false,
                        suggestions = suggestions,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
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

    fun getFavoriteCoinsDetails(
        ids: String
    ) {
        viewModelScope.launch {
            _favoriteCoinsDetailsState.update {
                it.copy(
                    isLoading = false,
                    details = emptyList()
                )
            }
            runCatching {
                getFavoriteCoinsDetailsUseCase(ids = ids)
            }.onSuccess { details ->
                _favoriteCoinsDetailsState.update {
                    it.copy(
                        isLoading = false,
                        details = details,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _favoriteCoinsDetailsState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message,
                    )
                }

            }
        }
    }

    fun clearFavoriteCoinsDetails() {
        _favoriteCoinsDetailsState.update {
            it.copy(
                details = emptyList(),
                error = null,
            )
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

    fun clearDetails() {
        _detailsState.update {
            it.copy(
                details = null,
                error = null,
            )
        }
    }

}