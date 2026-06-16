package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.SearchCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.presentation.widgets.SkeletonBox
import com.example.cryptotrack.presentation.widgets.TrendWidget
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.BlackNavigation
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.GreyCrossColor
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.SearchBarColor


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
            BottomBar(navController = navController)
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

    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadTrends()
    }

    LaunchedEffect(query) {
        if (query.isNotEmpty())
            viewModel.search(query = query)
        else {
            viewModel.clearSuggestionsList()
            isExpanded = false
        }
    }

    val coinsList by coinViewModel.coins.collectAsState(initial = emptyList())




    Column(
        modifier = Modifier
            .background(color = BlackBackground)
            .padding(paddingValues)
            .fillMaxSize()
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            if (coinsList.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }
                item {
                    SearchedCoinsList(
                        coins = coinsList,
                        navController = navController,
                        coinViewModel = coinViewModel,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
            }

            item {

                SuggestionList(
                    suggestions = suggestions.suggestions,
                    navController = navController,
                    coinViewModel = coinViewModel,
                    isExpanded = isExpanded,
                    onExpandedChange = {
                        isExpanded = it
                    }
                )
            }

            item {

                Spacer(modifier = Modifier.height(15.dp))
            }

            item {

                TrendWidget(
                    trends = trends.trendCoins,
                    navController = navController,
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
                color = DarkBlue,
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
                        if (query.isEmpty()) {
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
            if (query.isNotEmpty()) {
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
private fun SuggestionList(
    suggestions: Search?,
    navController: NavController,
    coinViewModel: CoinViewModel,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
) {

    val coins = suggestions?.coins


    val visibleCoins = if (isExpanded) {
        coins.orEmpty()
    } else {
        coins.orEmpty().take(7)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )

        ) {
            if (!coins.isNullOrEmpty()) {
                Column {
                    visibleCoins.forEachIndexed { index, coin ->
                        Suggestion(
                            coin = coin,
                            navController = navController,
                            coinViewModel = coinViewModel
                        )

                        if (index != visibleCoins.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(OutlineGray)
                            )
                        }
                    }
                }
            }
        }
        if (!coins.isNullOrEmpty()) {
            if ((coins.size) > 7) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(
                            color = DarkBlue,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = OutlineGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            onExpandedChange(!isExpanded)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = if (isExpanded) "Скрыть" else "Показать ещё",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(
                                if (isExpanded) R.drawable.ic_arrow_up
                                else R.drawable.ic_arrow_down
                            ),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(9.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Suggestion(
    coin: SearchCoin,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
                coinViewModel.insertCoin(id = coin.id, name = coin.name, path = coin.thumb)
            }
            .padding(horizontal = 10.dp)
            .height(36.dp)
            .fillMaxWidth()
            .background(color = DarkBlue),
    ) {
        if (coin.marketCapRank != null) {
            Text(
                text = coin.marketCapRank.toString(),
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.2f),
            )
        } else {
            Box(
                modifier = Modifier
                    .weight(0.2f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxHeight()
                .weight(1f),
        ) {
            AsyncImage(
                model = coin.thumb,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = coin.symbol,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

    }
}
@Composable
private fun SearchedCoin(
    coin: RoomCoin,
    navController: NavController,
    coinViewModel: CoinViewModel,
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
                    model = coin.path,
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
) {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        coins.asReversed().forEach { coin ->
            SearchedCoin(
                coin = coin,
                navController = navController,
                coinViewModel = coinViewModel,
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}


