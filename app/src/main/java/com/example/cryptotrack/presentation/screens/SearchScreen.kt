package com.example.cryptotrack.presentation.screens

import android.view.inputmethod.InlineSuggestion
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Query
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.SearchCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBarPreview
import com.example.cryptotrack.presentation.widgets.TrendCoinsWidget
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.BlackNavigation
import com.example.cryptotrack.ui.theme.GreyCrossColor
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.SearchBarColor
import kotlinx.coroutines.flow.Flow


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: CoinGeckoViewModel,
    coinViewModel: CoinViewModel,
) {
    Scaffold(
        contentColor = BlackBackground,
        topBar = {},
        bottomBar = {
            BottomBarPreview()
        }
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            navController = navController,
            viewModel = viewModel,
            coinViewModel = coinViewModel,
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun SearchScreenPreview() {
//    Scaffold(
//        contentColor = BlackBackground,
//        topBar = {},
//        bottomBar = {
//            BottomBarPreview()
//        }
//    ) { paddingValues ->
//        Content(paddingValues = paddingValues)
//    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: CoinGeckoViewModel,
    coinViewModel: CoinViewModel,
) {

    var query by remember { mutableStateOf("") }
    val suggestions by viewModel.searchState.collectAsState()
    val trends by viewModel.trendState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTrends()
    }

    LaunchedEffect(query) {
        if (query.isNotEmpty())
            viewModel.search(query = query)
        else
            viewModel.clearSuggestionsList()
    }

    val coinsList by coinViewModel.coins.collectAsState(initial = emptyList())



    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = BlackBackground)
            .padding(horizontal = 15.dp)
    ) {
        SearchField(
            query = query,
            onQueryChange = {
                query = it
            },
            onQueryClear = {
                query = ""
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            if(coinsList.isNotEmpty()) {
                item {
                    SearchedCoinsList(
                        coins = coinsList,
                        navController = navController,
                        viewModel = viewModel,
                        coinViewModel = coinViewModel,
                    )
                }
            }

            item {

                SuggestionList(
                    suggestions = suggestions.suggestions,
                    navController = navController,
                    coinViewModel = coinViewModel,
                )
            }

            item {

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {

                TrendCoinsWidget(
                    trends = trends.trendCoins,
                    navController = navController,
                    visibleCoins = 5,
                )
            }
        }
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryClear: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(
                color = SearchBarColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(15.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f),
                maxLines = 1,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Inter,
                    color = Color.White
                ),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (query.isNullOrEmpty()) {
                            Text(
                                text = "Search coin",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = Inter,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (!query.isNullOrEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_circle_cross),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            onQueryClear()
                        },
                )
            }
        }
    }
}

@Composable
fun SuggestionList(
    suggestions: Search?,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {

    var isExpanded by remember { mutableStateOf(false) }

    if (!suggestions?.coins.isNullOrEmpty()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color.White,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .background(
                        color = BlackBackground,
                        shape = RoundedCornerShape(30.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 15.dp, bottom = 5.dp)
                ) {

                    if (!isExpanded) {
                        suggestions.coins.take(5).forEach { coin ->
                            Suggestion(
                                coin = coin,
                                navController = navController,
                                coinViewModel = coinViewModel,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    } else {
                        suggestions.coins.forEach { coin ->
                            Suggestion(
                                coin = coin,
                                navController = navController,
                                coinViewModel = coinViewModel,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color.White,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .background(
                        color = if (isExpanded) BlackNavigation else SearchBarColor,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .clickable {
                        isExpanded = !isExpanded
                    }
            ) {
                Text(
                    text = "Показать еще",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Inter,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun Suggestion(
    coin: SearchCoin,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
            .clickable {
                coinViewModel.insertCoin(id = coin.id, name = coin.name)
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
            }
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "${coin.marketCapRank}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Inter,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.2f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        AsyncImage(
            model = coin.thumb,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = coin.symbol,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = coin.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}


@Composable
private fun SearchedCoin(
    coin: RoomCoin,
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel, // возможно подгружать картинку отсюда
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(27.dp)
            .background(
                color = BlackNavigation,
                shape = RoundedCornerShape(25.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 6.dp)
                .fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
                    }
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = coin.name,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(27.dp)
                    .clickable {
                        coinViewModel.deleteCoin(id = coin.id)
                    },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = null,
                    modifier = Modifier
                        .size(6.dp),
                    tint = GreyCrossColor,
                )
            }
        }
    }
}

@Composable
private fun SearchedCoinsList(
    coins: List<RoomCoin>,
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
) {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        coins.forEach { coin->
            SearchedCoin(
                coin = coin,
                navController = navController,
                coinViewModel = coinViewModel,
                viewModel = viewModel
            )
        }
    }
}
