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
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.SearchBarColor


@Composable
fun ProfileScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
) {
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomBar(
                navController = navController,
            )
        },
    ) {paddingValues ->
        Content(
            paddingValues = paddingValues,
            coinViewModel = coinViewModel,
            navController = navController,
        )
    }
}


@Composable
private fun Content(
    paddingValues: PaddingValues,
    coinViewModel: CoinViewModel,
    navController: NavController,
) {

    val historyOfViewingList by coinViewModel.historyOfViewingCoins.collectAsState(initial = emptyList())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlackBackground)
            .padding(horizontal = 15.dp)
            .padding(paddingValues)
    ) {
        UserInfo()
        UserStatsWidget()
        RecentlyViewed(coins = historyOfViewingList, navController = navController)
        FavoriteItem()
    }

}

@Composable
private fun UserInfo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_reddit),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Danya",
            fontSize = 28.sp,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FavoriteWidget() {

}

@Composable
private fun FavoriteItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 10.dp)
    ) {
        AsyncImage(
            model = "", // подгружать
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .weight(0.1f)

        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.8f)
        ) {
            Text(
                text = "Bitcoin",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "BTC",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = "$67 812.59",
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            textAlign = TextAlign.Right,
            text = "+2.35%",
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Green,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.25f)
        )

    }


}

@Composable
private fun UserStatsWidget() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(start = 100.dp)
            .fillMaxWidth()
    ) {
        StatsItem(
            icon = R.drawable.ic_star,
            title = "Избранные",
            value = 12,
        )
        StatsItem(
            icon = R.drawable.ic_graph_up,
            title = "Отслеживается",
            value = 0,
        )
        StatsItem(
            icon = R.drawable.ic_history,
            title = "Просмотрено",
            value = 24,
        )
    }
}

@Composable
private fun StatsItem(
    icon: Int,
    title: String,
    value: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = SearchBarColor,
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value.toString(),
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
private fun RecentlyViewed(
    coins: List<HistoryOfViewingCoin>,
    navController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Недавно просмотренные",
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            coins.asReversed().forEach { coin ->
                RecentlyViewedItem(
                    coin = coin,
                    navController = navController,
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
private fun RecentlyViewedItem(
    coin: HistoryOfViewingCoin,
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(50.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable{
                navController.navigate(Screen.CoinDetails.createRoute(id = coin?.id ?: ""))
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            AsyncImage(
                model = coin.imageUrl, // подгружать
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Text(
                    text = "ETH",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }
    }
}