package com.example.cryptotrack.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.SearchCoin
import com.example.cryptotrack.presentation.util.price.formatRate
import com.example.cryptotrack.presentation.util.price.sanitizeAmount
import com.example.cryptotrack.presentation.util.price.sanitizePrice
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.AddPurchaseTopAppBar
import com.example.cryptotrack.presentation.widgets.SkeletonBox
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.GreenForButton
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.OutlineGreen
import com.example.cryptotrack.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.forEachIndexed
import kotlin.collections.lastIndex
import kotlin.collections.orEmpty
import kotlin.collections.take


@Composable
fun AddPurchaseScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
) {
    Scaffold(
        topBar = {
            AddPurchaseTopAppBar(
                navController = navController,
            )
        },
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            navController = navController,
            coinViewModel = coinViewModel,
            viewModel = viewModel,
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun PurchaseScreenPreview() {
//    Scaffold(
//    ) { paddingValues ->
//        Content(
//            paddingValues = paddingValues
//        )
//    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
    navController: NavController,
) {

    val suggestions by viewModel.searchState.collectAsState()
    var query by remember { mutableStateOf("") }
    var coinsCount by remember { mutableStateOf("") }
    var buyPrice by remember { mutableStateOf("") }

    var currentCoinId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearFavoriteCoinsDetails()
    }

    LaunchedEffect(query) {
        if (query.isNotEmpty())
            viewModel.search(query = query)
        else {
            viewModel.clearSuggestionsList()
        }

    }

    LaunchedEffect(currentCoinId) {
        if (currentCoinId.isNotEmpty()) {
            viewModel.getFavoriteCoinsDetails(ids = currentCoinId)
        }
    }

    val favoriteCoinState by viewModel.favoriteCoinsDetailsState.collectAsState()

    LaunchedEffect(favoriteCoinState) {
        Log.d("Add Purchase Screen", "purchaseState: $favoriteCoinState")
    }

    val isRateLimited = favoriteCoinState.error?.contains("429") == true

    val selectedCoin = favoriteCoinState.details?.firstOrNull()

    var buyDate by remember { mutableStateOf<Long?>(null) }

    val coins = coinsCount.toDoubleOrNull()
    val price = buyPrice.toDoubleOrNull()

    val isDateValid = buyDate != null && buyDate!! > 0

    val isFormValid =
        selectedCoin != null &&
                coins != null &&
                price != null &&
                coins > 0 &&
                price > 0 &&
                isDateValid

    val showFinalCost =
        coins != null &&
                price != null &&
                coins > 0 &&
                price > 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .padding(paddingValues)
            .padding(horizontal = 15.dp),
    ) {
        if (favoriteCoinState.details.isNullOrEmpty()) {
            if (isRateLimited || favoriteCoinState.isLoading) {
                item {
                    SkeletonSelectedCoin(
                        onClick = {
                            viewModel.clearFavoriteCoinsDetails()
                        }
                    )
                }
            } else {
                item {
                    SearchCoinField(
                        query = query,
                        onQueryChange = {
                            query = it
                        },
                        onQueryClear = {
                            query = ""
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    if ((suggestions.suggestions?.coins.isNullOrEmpty() && query.isNotEmpty()) && suggestions.isLoading) {
                        SkeletonSuggestionList()
                    } else {
                        SuggestionList(
                            suggestions = suggestions.suggestions,
                            onCoinClick = { coin ->
                                query = coin.name

                                viewModel.clearSuggestionsList()

                                viewModel.getFavoriteCoinsDetails(ids = coin.id)
                            }
                        )
                    }
                }
            }
        } else {
            if (selectedCoin != null) {
                item {
                    SelectedCoin(
                        details = selectedCoin,
                        onClick = {
                            viewModel.clearFavoriteCoinsDetails()
                        }
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            CountInputField(
                query = coinsCount,
                title = selectedCoin?.symbol?.uppercase() ?: "",
                onQueryChange = {
                    coinsCount = sanitizeAmount(it)
                },
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            PriceInputField(
                query = buyPrice,
                symbol = selectedCoin?.symbol?.uppercase() ?: "монету",
                onQueryChange = {
                    buyPrice = sanitizePrice(it)
                },
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            CalendarField(
                buyDate = buyDate,
                onDateSelected = {
                    buyDate = it
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (showFinalCost) {
            item {
                FinalCost(
                    symbol = selectedCoin?.symbol ?: "",
                    count = coinsCount,
                    price = buyPrice,
                )
            }
        }


        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            AcceptButton(
                isFormValid = isFormValid,
                onClick = {
                    coinViewModel.insertPurchase(
                        coinId = selectedCoin?.id ?: "",
                        name = selectedCoin?.name ?: "",
                        amount = coinsCount.toDouble(),
                        buyPrice = buyPrice.toDouble(),
                        buyDate = buyDate ?: 0,
                        imageUrl = selectedCoin?.image ?: "",
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}


@Composable
private fun SearchCoinField(
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
    onCoinClick: (SearchCoin) -> Unit,
) {

    val coins = suggestions?.coins


    val visibleCoins = coins.orEmpty().take(7)

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
                            onClick = {
                                onCoinClick(coin)
                            }
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
    }
}

@Composable
private fun SkeletonSuggestionList() {


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
            Column {
                for (i in 0..6) {
                    SkeletonSuggestion()
                    if (i != 6) {
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
}

@Composable
private fun Suggestion(
    coin: SearchCoin,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClick()
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
            Text(
                text = "0",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.2f),
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
private fun SkeletonSuggestion() {
    SkeletonBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
    )
}


@Composable
private fun SelectedCoin(
    details: FavoriteCoinDetails,
    onClick: () -> Unit,
) {

    val currentPriceFormatted = formatRate(value = details.currentPrice)

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val percentageColor = if (details.priceChangePercentage24h >= 0) Green else Red

    val formatter = DecimalFormat("#,##0.00", symbols)

    val percentageText = details.priceChangePercentage24h.let {
        if (it > 0) {
            "+${formatter.format(it)}%"
        } else {
            "${formatter.format(it)}%"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .height(70.dp)
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    AsyncImage(
                        model = details.image,
                        contentDescription = null,
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = details.name,
                            fontFamily = Inter,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = details.symbol.toUpperCase(Locale.ROOT),
                            fontFamily = Inter,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "$$currentPriceFormatted",
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = percentageText,
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = percentageColor,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Выбрать другую монету",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun SkeletonSelectedCoin(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .height(70.dp)
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
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        SkeletonBox(
                            modifier = Modifier
                                .height(17.dp)
                                .width(100.dp)
                        )
                        SkeletonBox(
                            modifier = Modifier
                                .height(14.dp)
                                .width(30.dp)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .height(17.dp)
                            .width(100.dp)
                    )
                    SkeletonBox(
                        modifier = Modifier
                            .height(14.dp)
                            .width(35.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Выбрать другую монету",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun CountInputField(
    query: String,
    title: String,
    onQueryChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Количество",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Inter,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    color = OutlineGray,
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
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
                                    text = "Количество монет",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = Inter,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Text(
                    textAlign = TextAlign.End,
                    text = title,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
    }
}

@Composable
private fun PriceInputField(
    query: String,
    symbol: String,
    onQueryChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Цена покупки",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Inter,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    color = OutlineGray,
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
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
                                    text = "Цена за 1 $symbol",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = Inter,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Text(
                    textAlign = TextAlign.End,
                    text = "USD",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Цена за 1 $symbol",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Inter,
            color = Color.Gray,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarField(
    buyDate: Long?,
    onDateSelected: (Long) -> Unit,
) {

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var selectedDateMillis by remember {
        mutableStateOf<Long?>(null)
    }

    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState()

    val text = remember(buyDate) {
        if (buyDate == null) {
            "Выберите дату"
        } else {
            SimpleDateFormat(
                "dd.MM.yyyy HH:mm",
                Locale.getDefault()
            ).format(Date(buyDate))
        }
    }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Дата покупки",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Inter,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    color = OutlineGray,
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    showDatePicker = true
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Inter,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
    if (showDatePicker) {


        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                Button(

                    onClick = {

                        selectedDateMillis =
                            dateState.selectedDateMillis

                        showDatePicker = false

                        if (selectedDateMillis != null) {
                            showTimePicker = true
                        }

                    }

                ) {

                    Text("OK")

                }

            }

        ) {

            DatePicker(
                state = dateState
            )

        }

    }

    if (showTimePicker) {

        AlertDialog(

            onDismissRequest = {
                showTimePicker = false
            },

            confirmButton = {

                Button(

                    onClick = {

                        selectedDateMillis?.let { date ->
                            val calendar = Calendar.getInstance()

                            calendar.timeInMillis = date

                            calendar.set(Calendar.HOUR_OF_DAY, timeState.hour)
                            calendar.set(Calendar.MINUTE, timeState.minute)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            onDateSelected(calendar.timeInMillis)

                            showTimePicker = false
                        }

                    }

                ) {

                    Text("OK")

                }

            },

            dismissButton = {

                Button(

                    onClick = {

                        showTimePicker = false

                    }

                ) {

                    Text("Cancel")

                }

            },

            text = {

                TimePicker(
                    state = timeState
                )

            }

        )

    }
}


@Composable
private fun FinalCost(
    symbol: String,
    count: String,
    price: String,
) {


    val total = (count.toDoubleOrNull() ?: 0.0) *
            (price.toDoubleOrNull() ?: 0.0)

    val formattedTotal = formatRate(value = total)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
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
                .padding(all = 15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = "Итого",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White,
                )
                Text(
                    text = "$count ${symbol.uppercase()} по цене $price",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = "$$formattedTotal",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Green,
                )
            }
        }
    }
}

@Composable
private fun AcceptButton(
    isFormValid: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = if (!isFormValid) DarkBlue else GreenForButton,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = if (!isFormValid) OutlineGray else OutlineGreen,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                if (isFormValid) {
                    onClick()
                }
            }
    ) {
        Text(
            text = "Сохранить покупку",
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = if (!isFormValid) Color.White else Color.Black,
        )
    }
}