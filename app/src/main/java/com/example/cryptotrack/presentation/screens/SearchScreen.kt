package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.widgets.BottomBarPreview
import com.example.cryptotrack.presentation.widgets.TrendCoinsWidget
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.BlackNavigation
import com.example.cryptotrack.ui.theme.GreyCrossColor
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.SearchBarColor


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: CoinGeckoViewModel,
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
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = BlackBackground)
            .padding(horizontal = 15.dp)
    ) {
        SearchField()
        Spacer(modifier = Modifier.height(20.dp))
        SuggestionList()
        Spacer(modifier = Modifier.height(20.dp))
        TrendCoinsWidget(
            trends = null,
            navController = navController,
            visibleCoins = 5,
        )
    }
}

@Composable
fun SearchField() {
    val text = remember { mutableStateOf("") }
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
                value = text.value,
                onValueChange = { text.value = it },
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
                        if (text.value.isNullOrEmpty()) {
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
            if (!text.value.isNullOrEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_circle_cross),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp),
                )
            }
        }
    }
}

@Composable
fun SuggestionList() {
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
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {
            Suggestion()
            Spacer(modifier = Modifier.height(10.dp))
            Suggestion()
            Spacer(modifier = Modifier.height(10.dp))
            Suggestion()
        }
    }
}


@Composable
fun Suggestion() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
//                AsyncImage(
//                model = "https://assets.coingecko.com/coins/images/28470/standard/MTLOGO.png?1696527464",
//                contentDescription = null,
//                modifier = Modifier.size(30.dp),
//            )
        Icon(
            painter = painterResource(R.drawable.bitcoin),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .weight(0.3f)
                .size(30.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = "BTC",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Bitcoin",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
                .padding(end = 15.dp)
        ) {
            Text(
                text = "$126,080",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = Color.White,
                maxLines = 1,
            )
            Row() {
                Text(
                    text = "2.1%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Inter,
                    color = Green,
                    maxLines = 1,
                )
                Icon(
                    painter = painterResource(R.drawable.ic_up),
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier
                        .size(15.dp)
                        .padding(start = 3.dp)
                )
            }
        }
    }
}


@Composable
private fun SearchedCoin() {
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
                .padding(start = 6.dp, end = 10.dp)
                .fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable{
                        // navigate to detail screen by id
                    }
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Bitcoin",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.width(11.dp))
            }
            Icon(
                painter = painterResource(R.drawable.ic_cross),
                contentDescription = null,
                modifier = Modifier.size(6.dp),
                tint = GreyCrossColor,
            )
        }
    }
}
