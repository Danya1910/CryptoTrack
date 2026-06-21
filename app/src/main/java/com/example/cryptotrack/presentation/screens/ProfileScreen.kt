package com.example.cryptotrack.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.domain.model.UserData
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.aggregatePurchases
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.presentation.util.price.formatTime
import com.example.cryptotrack.presentation.util.uiModels.FavoriteUiItem
import com.example.cryptotrack.presentation.util.uiModels.Slice
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.viewmodel.UserViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.presentation.widgets.SkeletonBox
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Orange
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.SearchBarColor
import kotlinx.coroutines.delay
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


@Composable
fun ProfileScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
    userViewModel: UserViewModel,
) {

    var snackbarTrigger by remember { mutableStateOf(0) }

    val isSnackbarVisible = snackbarTrigger > 0

    LaunchedEffect(snackbarTrigger) {
        if (snackbarTrigger > 0) {
            delay(3500)
            snackbarTrigger = 0
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {
            BottomBar(
                navController = navController,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Content(
                paddingValues = paddingValues,
                coinViewModel = coinViewModel,
                navController = navController,
                viewModel = viewModel,
                userViewModel = userViewModel,
                onShowSnackbar = {
                    snackbarTrigger++
                },
            )
            SnakeBar(
                visible = isSnackbarVisible,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            )
        }
    }
}


@Composable
private fun Content(
    paddingValues: PaddingValues,
    coinViewModel: CoinViewModel,
    navController: NavController,
    viewModel: CoinGeckoViewModel,
    userViewModel: UserViewModel,
    onShowSnackbar: () -> Unit,
) {

    val historyOfViewingList by coinViewModel.historyOfViewingCoins.collectAsState(initial = emptyList())
    val favoriteCoins by coinViewModel.favoriteCoins.collectAsState(initial = emptyList())
    val purchase by coinViewModel.purchase.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        userViewModel.getData()
    }

    val userData by userViewModel.userData.collectAsState()

    LaunchedEffect(favoriteCoins) {
        if (favoriteCoins.isNotEmpty()) {
            val ids = favoriteCoins.joinToString(",") { it.id }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val aggregatedPurchase = remember(purchase) {
        aggregatePurchases(purchase)
    }

    LaunchedEffect(aggregatedPurchase) {
        if (purchase.isNotEmpty()) {
            val ids = aggregatedPurchase.joinToString(",") { it.coinId }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val purchaseDetailsState by viewModel.favoriteCoinsDetailsState.collectAsState()

    val purchaseDetails = purchaseDetailsState.details

    val isApiDataAvailable = !purchaseDetails.isNullOrEmpty()


    val investedSum = calculateInvested(purchases = purchase)

    val currentSum = if (isApiDataAvailable) {
        calculateCurrentPrice(
            aggregatedPurchase = aggregatedPurchase,
            details = purchaseDetails
        )
    } else null

    val currentFormatted = formatPrice(value = currentSum)

    val profitPercentage = currentSum?.let {
        calculateProfitPercentage(
            current = currentSum,
            invested = investedSum,
        )
    }

    val favoriteCoinsDetails by viewModel.favoriteCoinsDetailsState.collectAsState()


    val details = favoriteCoinsDetails.details?.reversed().orEmpty()

    val uiList: List<FavoriteUiItem> = if (favoriteCoinsDetails.details.isNullOrEmpty()) {
        favoriteCoins.map { FavoriteUiItem.Basic(it) }
    } else {
        details.map { FavoriteUiItem.Full(it) }
    }


    val favoritesCount = favoriteCoins.size
    val recentlyViewedCount = historyOfViewingList.size

    val parts = createSlices(purchases = purchase)

    val time = purchase.minOfOrNull { it.buyDate }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlackBackground)
            .padding(horizontal = 15.dp)
            .padding(paddingValues)
    ) {
        UserInfo(
            userViewModel = userViewModel,
            userData = userData,
            navController = navController,
        )
        UserStatsWidget(
            favoritesCount = favoritesCount,
            recentlyViewedCount = recentlyViewedCount,

            )
        Spacer(modifier = Modifier.height(10.dp))
        PurchaseWidget(
            parts = parts,
            navController = navController,
            currentPrice = currentFormatted,
            currentSum = currentSum,
            investedSum = investedSum,
            profitPercentage = profitPercentage,
            time = time,
            isLoading = purchaseDetailsState.isLoading,
            onShowSnackbar = {
                onShowSnackbar()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        FavoriteWidget(
            details = uiList,
            navController = navController,
        )
        Spacer(modifier = Modifier.height(10.dp))
        RecentlyViewed(coins = historyOfViewingList, navController = navController)
    }
}

@Composable
private fun SnakeBar(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = {it}) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = {it}) + fadeOut(),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(bottom = 15.dp)
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = OutlineGray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = "Сумма вложений",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun UserInfo(
    userViewModel: UserViewModel,
    userData: UserData?,
    navController: NavController,
) {
    var query by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            userViewModel.insertAvatar(context, uri)
            navController.navigate(Screen.Profile.route)

        }
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        if (userData?.avatar.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_incognito),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .clickable {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 43.dp, y = 25.dp)
                        .size(25.dp)
                        .background(
                            color = DarkBlue,
                            shape = CircleShape,
                        )
                        .clickable {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp)
            ) {
                AsyncImage(
                    model = File(userData.avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.Center)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 43.dp, y = 25.dp)
                        .size(25.dp)
                        .background(
                            color = DarkBlue,
                            shape = CircleShape,
                        )
                        .clickable {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }

            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            if (isEditing) {
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    cursorBrush = SolidColor(Color.White),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box {
                            if (query == "") {
                                Text(
                                    text = userData?.name ?: "Введите имя",
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color.White,
                                start = Offset(x = 0f, y = size.height),
                                end = Offset(x = size.width, y = size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                )
            } else {
                Text(
                    text = userData?.name ?: "Name",
                    fontSize = 22.sp,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(25.dp)
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
                        if (isEditing) {
                            if (query == "") {
                                userViewModel.insertName(name = userData?.name ?: "Введите имя")
                            } else {
                                userViewModel.insertName(name = query)
                            }
                            query = ""
                        }
                        isEditing = !isEditing

                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (isEditing) R.drawable.ic_tick
                            else R.drawable.ic_edit
                        ),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isEditing) "Сохранить" else "Изменить",
                        fontSize = 9.sp,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                    )
                }
            }
        }

    }
}

@Composable
private fun FavoriteWidget(
    details: List<FavoriteUiItem>?,
    navController: NavController,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp)
        ) {
            Text(
                text = "Избранные монеты",
                fontFamily = Inter,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Смотреть все",
                fontFamily = Inter,
                color = Green,
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Favorites.route)
                    }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = OutlineGray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val coins = details?.take(4).orEmpty()

                coins.forEachIndexed { index, item ->

                    when (item) {

                        is FavoriteUiItem.Full -> {
                            FavoriteItemFull(
                                coin = item.data,
                                navController = navController
                            )
                        }

                        is FavoriteUiItem.Basic -> {
                            FavoriteItemBasic(
                                coin = item.data,
                                navController = navController
                            )
                        }
                    }

                    if (index != coins.lastIndex) {
                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(color = OutlineGray)
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun FavoriteItemFull(
    coin: FavoriteCoinDetails,
    navController: NavController,
) {

    val price = formatPrice(value = coin.currentPrice)

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val percentageColor = if (coin.priceChangePercentage24h >= 0) Green else Red

    val formatter = DecimalFormat("#,##0.00", symbols)

    val percentageText = coin.priceChangePercentage24h.let {
        if (it > 0) {
            "+${formatter.format(it)}%"
        } else {
            "${formatter.format(it)}%"
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
            }
            .padding(horizontal = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxHeight()
                .weight(1f),
        ) {
            AsyncImage(
                model = coin.image,
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
        Text(
            text = "$$price",
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            textAlign = TextAlign.Right,
            text = percentageText,
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = percentageColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.3f)
        )

    }
}

@Composable
private fun FavoriteItemBasic(
    coin: FavoriteCoin,
    navController: NavController,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
            }
            .padding(horizontal = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxHeight()
                .weight(1f),
        ) {
            AsyncImage(
                model = coin.imageUrl,
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
private fun UserStatsWidget(
    favoritesCount: Int,
    recentlyViewedCount: Int,
) {
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
            value = favoritesCount,
        )
        StatsItem(
            icon = R.drawable.ic_graph_up,
            title = "Отслеживается",
            value = 0,
        )
        StatsItem(
            icon = R.drawable.ic_history,
            title = "Просмотрено",
            value = recentlyViewedCount,
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(5.dp))
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
                Spacer(modifier = Modifier.width(5.dp))
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
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            AsyncImage(
                model = coin.imageUrl,
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
                    text = coin.symbol.uppercase(),
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
private fun PurchaseWidget(
    parts: List<Slice>,
    navController: NavController,
    currentPrice: String,
    currentSum: Double?,
    profitPercentage: Double?,
    investedSum: Double,
    time: Long?,
    isLoading: Boolean,
    onShowSnackbar: () -> Unit,
) {

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val percentageColor = profitPercentage?.let { if (profitPercentage >= 0) Green else Red }

    val formatter = DecimalFormat("#,##0.00", symbols)

    val percentageText = profitPercentage?.let {
        if (profitPercentage >= 0) " +${formatter.format(profitPercentage)}%" else
            formatter.format(profitPercentage) + "%"
    }

    val firstInvestedDate = formatTime(millis = time ?: 0)

    val investedPrice = formatPrice(value = investedSum)

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                navController.navigate(Screen.Purchase.route)
            }
            .padding(all = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(0.6f)
            ) {
                Text(
                    text = "Стоимость портфеля",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(10.dp))
                if(isLoading) {
                    SkeletonBox(
                        modifier = Modifier
                            .height(22.dp)
                            .fillMaxWidth(0.8f)
                    )
                } else {
                    if (investedSum == 0.0) {
                        Text(
                            text = "$0.0",
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color.White,
                        )
                    } else {
                        if (currentSum == null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$$investedPrice",
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    painter = painterResource(R.drawable.ic_clock),
                                    contentDescription = null,
                                    tint = Orange,
                                    modifier = Modifier
                                        .size(11.dp)
                                        .clickable{
                                            onShowSnackbar()
                                        }
                                )
                            }
                        } else {
                            Text(
                                text = "$$currentPrice",
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = Color.White,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (investedSum == 0.0) {
                    Text(
                        text = "0.0%",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                } else {
                    if (percentageText == null || percentageColor == null) {
                        SkeletonBox(
                            modifier = Modifier
                                .height(14.dp)
                                .width(50.dp)
                        )
                    } else {
                        Text(
                            text = percentageText,
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = percentageColor,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (investedSum == 0.0) {
                    Text(
                        text = "Запишите покупку!",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        color = Color.Gray,
                    )
                } else {
                    if (firstInvestedDate.isEmpty()) {
                        SkeletonBox(
                            modifier = Modifier
                                .height(12.dp)
                                .width(80.dp)
                        )
                    } else {
                        Text(
                            text = "C $firstInvestedDate",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp,
                            color = Color.Gray,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                PieTest(slices = parts)
                Spacer(modifier = Modifier.height(10.dp))
                PurchasePercentage(parts = parts)
            }
        }
    }
}

@Composable
fun PieTest(
    slices: List<Slice>,

    ) {

    val gap = 6f

    Canvas(
        modifier = Modifier.size(50.dp)
    ) {

        val total = slices.sumOf { it.value.toDouble() }.toFloat()
        var startAngle = -90f
        val stroke = 10.dp.toPx()

        slices.forEach {

            val sweep = it.value / total * 360f

            drawArc(
                color = it.color,
                startAngle = startAngle + gap / 2,
                sweepAngle = sweep - gap,
                useCenter = false,
                style = Stroke(width = stroke)
            )

            startAngle += sweep
        }
    }
}

@Composable

private fun PurchasePercentage(
    parts: List<Slice>,
) {
    val total = parts.sumOf { it.value.toDouble() }.toFloat()

    Column {
        parts.forEach { coin ->
            val percent = if (total > 0f) {
                coin.value / total * 100
            } else {
                0f
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            color = coin.color,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(0.8f),
                )
                Text(
                    text = "%.1f%%".format(percent).replace(',', '.'),
                    textAlign = TextAlign.End,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(0.3f),
                )
            }
        }
    }
}

fun createSlices(purchases: List<PurchaseCoin>): List<Slice> {

    val colors = listOf(
        Color(0xFF4F46E5),
        Color(0xFF22C55E),
        Color(0xFFF97316),
        Color(0xFFEAB308),
        Color(0xFF06B6D4),
        Color(0xFFEC4899)
    )

    // Объединяем одинаковые монеты и считаем их стоимость
    val grouped = purchases
        .groupBy { it.coinId }
        .map { (_, list) ->
            Slice(
                name = list.first().name,
                value = list.sumOf { it.amount * it.buyPrice }.toFloat(),
                color = Color.Transparent
            )
        }
        .sortedByDescending { it.value }

    // Оставляем 3 самые большие
    val result = mutableListOf<Slice>()

    grouped.take(3).forEachIndexed { index, slice ->
        result.add(
            slice.copy(
                color = colors[index % colors.size]
            )
        )
    }

    // Все остальные объединяем в "Другие"
    val otherValue = grouped
        .drop(3)
        .sumOf { it.value.toDouble() }
        .toFloat()

    if (otherValue > 0f) {
        result.add(
            Slice(
                name = "Другие",
                value = otherValue,
                color = Color(0xFFEF4444)
            )
        )
    }

    return result
}