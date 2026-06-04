package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.HelpDarkBlue
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.Yellow


@Composable
fun FavoritesScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
) {
    Scaffold(
        topBar = {},
        bottomBar = {},
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            navController = navController,
            coinViewModel = coinViewModel

        )
    }
}

@Composable
@Preview(showBackground = true)
private fun FavoritesScreenPreview() {
//    Scaffold(
//        topBar = {},
//        bottomBar = {},
//    ) { paddingValues ->
//        Content(
//            paddingValues = paddingValues,
//            navController = NavController(),
//            coinViewModel = CoinViewModel()
//
//        )
//    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {

    val favoriteCoins by coinViewModel.favoriteCoins.collectAsState(initial = emptyList())

    val removedIds = remember { mutableStateListOf<String>() }

    val isFavoriteEmpty = favoriteCoins.isEmpty()

    DisposableEffect(Unit) {
        onDispose {
            removedIds.forEach { id ->
                coinViewModel.deleteFavoriteCoin(id)
            }
        }
    }

    Column(
        modifier = Modifier
            .background(color = BlackBackground)
            .padding(paddingValues = paddingValues)
            .padding(horizontal = 15.dp)
            .fillMaxSize()
    ) {
        if (isFavoriteEmpty) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HelpWidget(
                    navController = navController,
                )
            }

        } else {
            FavoriteHat(
                favoriteCoins = favoriteCoins,
                coinViewModel = coinViewModel,
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        CoinsList(
            navController = navController,
            favoriteCoins = favoriteCoins,
            coinViewModel = coinViewModel,
            removedIds = removedIds,
        )
    }
}

@Composable
private fun CoinsList(
    favoriteCoins: List<FavoriteCoin>,
    navController: NavController,
    coinViewModel: CoinViewModel,
    removedIds: MutableList<String>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(favoriteCoins) { coin ->
            CoinItem(
                coin = coin,
                coinViewModel = coinViewModel,
                navController = navController,
                modifier = Modifier.fillMaxWidth(),
                removedIds = removedIds,
            )
        }
    }
}

@Composable
private fun CoinItem(
    modifier: Modifier,
    coin: FavoriteCoin,
    navController: NavController,
    coinViewModel: CoinViewModel,
    removedIds: MutableList<String>,
) {

    val isFavorite = coin.id !in removedIds


    Box(
        modifier = modifier
            .height(70.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp)
        ) {
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
                Text(
                    text = coin.symbol.uppercase(),
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource
                        (
                        if (isFavorite) R.drawable.ic_fill_star else R.drawable.ic_star
                    ),
                    contentDescription = null,
                    tint = if (isFavorite) Yellow else Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable {
                            if (coin.id in removedIds) {
                                removedIds.remove(coin.id)
                            } else {
                                removedIds.add(coin.id)
                            }
                        }
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun FavoriteHat(
    favoriteCoins: List<FavoriteCoin>,
    coinViewModel: CoinViewModel,
) {

    val countsOfCoins = getCoinsCountText(favoriteCoins.size)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(all = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = HelpDarkBlue,
                        shape = CircleShape
                    )
                    .padding(all = 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_huge_star),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Text(
                    text = "Избранные монеты",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
                Text(
                    text = countsOfCoins,
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Green,
                )
                Text(
                    text = "Ваш список любимых криптовалют. Следите за ними и не упускайте самое важное.",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 5.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(30.dp)
                        .background(
                            color = HelpDarkBlue,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = OutlineGray,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .clickable {
                            coinViewModel.deleteAllFavoriteCoins()
                        }
                        .padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trashbox),
                            contentDescription = null,
                            tint = Red,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Очистить",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                            color = Red,
                        )
                    }
                }
            }
        }
    }
}

private fun getCoinsCountText(count: Int): String {
    val mod10 = count % 10
    val mod100 = count % 100

    return when {
        mod100 in 11..14 -> "$count монет"
        mod10 == 1 -> "$count монета"
        mod10 in 2..4 -> "$count монеты"
        else -> "$count монет"
    }
}


@Composable
private fun HelpWidget(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .drawBehind {
                drawRoundRect(
                    color = OutlineGray,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 8f)
                        )
                    ),
                    cornerRadius = CornerRadius(
                        x = 10.dp.toPx(),
                        y = 10.dp.toPx()
                    )
                )
            }
            .padding(all = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 100.dp, end = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Нет избранных монет",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Добавляйте монеты из раздела «рынок», чтобы они появлялись здесь.",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(50.dp),
            )
        }
    }
}