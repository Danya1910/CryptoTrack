package com.example.cryptotrack.presentation.screens

import android.bluetooth.le.ScanRecord
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.cryptotrack.R
import com.example.cryptotrack.presentation.widgets.BottomBarPreview
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.SearchBarColor


@Composable
fun CoinDetailScreen() {
}

@Composable
@Preview(showBackground = true)
private fun CoinDetailScreenPreview() {

    Scaffold(
        topBar = {},
        bottomBar = {
            BottomBarPreview()
        }
    ) { paddingValues ->
        Content(paddingValues = paddingValues)
    }

}

@Composable
private fun Content(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BlackBackground
            )
            .padding(horizontal = 15.dp)
            .padding(paddingValues)
    ) {
        CoinHat()
        DailyPrice(
            low24h = "1523.323",
            high24h = "2343.12",
            currentPrice = "2243.4435"
        )
        Spacer(modifier = Modifier.height(20.dp))
        CoinInfo()
        Spacer(modifier = Modifier.height(20.dp))
        CommunityBlock()
    }
}

@Composable
private fun CoinHat() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {

//           AsyncImage(
//                model = "https://assets.coingecko.com/coins/images/28470/standard/MTLOGO.png?1696527464",
//                contentDescription = null,
//                modifier = Modifier.size(25.dp),
//            )
        Icon(
            painter = painterResource(R.drawable.bitcoin),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Bitcoin",
            fontFamily = Inter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            letterSpacing = 1.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 120.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Btc",
            fontFamily = Inter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Price",
            fontFamily = Inter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,

            )
        Spacer(modifier = Modifier.width(10.dp))
        CoinNumber(number = 123)
    }
    Spacer(modifier = Modifier.height(25.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = "75,987.70 $",
            fontFamily = Inter,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_up),
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = Green,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "12.3% (24H)",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Green,
                letterSpacing = 1.sp,
            )
        }
    }
}

@Composable
private fun CoinNumber(
    number: Int
) {
    val displayNumber = if (number >= 1000000) {
        "1M+"
    } else if (number >= 100000) {
        "100K+"

    } else if (number >= 10000) {
        "9999+"

    } else {
        number.toString()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(22.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .shadow(
                elevation = 8.dp,
                spotColor = Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                horizontal = 7.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = "№ $displayNumber",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
        )
    }
}

@Composable
private fun DailyPrice(
    low24h: String,
    high24h: String,
    currentPrice: String,
) {

    val low = low24h.toFloat()
    val high = high24h.toFloat()
    val current = currentPrice.toFloat()

    val progress = ((current - low) / (high - low)).coerceIn(0f, 1f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .height(8.dp)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF373737)
                )
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEEFF00),
                                Color(0xFF00FF51),
                            )
                        )
                    )
                    .fillMaxWidth(progress)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "$$low24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$$high24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CoinInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Информация",
            fontFamily = Inter,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    shape = RoundedCornerShape(30.dp),
                    elevation = 4.dp,
                    spotColor = Color.White,
                )
                .background(
                    color = BlackBackground,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Рыночная капитализация",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Количество в обращении",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                ) {
                    Text(
                        text = "0,00 $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "0",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Полностью разбавленная оценка",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Общее предложение",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                ) {
                    Text(
                        text = "1 490,64 $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "0.000000000014 $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Максимальное предложение",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Объем торгов",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                ) {
                    Text(
                        text = "?",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "0,00 $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityBlock() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            //           AsyncImage(
//                model = "https://assets.coingecko.com/coins/images/28470/standard/MTLOGO.png?1696527464",
//                contentDescription = null,
//                modifier = Modifier.size(25.dp),
//            )
            Icon(
                painter = painterResource(R.drawable.bitcoin),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Официальный сайт",
                textAlign = TextAlign.Start,
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )
        }
    }
}