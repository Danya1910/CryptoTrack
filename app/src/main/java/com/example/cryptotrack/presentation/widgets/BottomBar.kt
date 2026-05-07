package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cryptotrack.R
import com.example.cryptotrack.ui.theme.BlackNavigation


@Composable
fun BottomBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavComponents.Market,
        BottomNavComponents.Search,
        BottomNavComponents.Profile
    )

    NavigationBar(
        containerColor = BlackNavigation,
        modifier = Modifier
            .shadow(
                elevation = 2.dp,
                spotColor = Color.White,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        ) {
            items.forEach { item ->
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .clickable{
                            navController.navigate(item.route)
                        }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF292929)
fun BottomBarPreview(
) {

    val items = listOf(
        BottomNavComponents.Market,
        BottomNavComponents.Search,
        BottomNavComponents.Profile
    )



    NavigationBar(
        containerColor = BlackNavigation,
        modifier = Modifier
            .shadow(
                elevation = 2.dp,
                spotColor = Color.White,
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        ) {
            items.forEach { item ->
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
    Text(
        text = "Preview!!!",
        fontSize = 15.sp,
        color = Color.White
    )
}