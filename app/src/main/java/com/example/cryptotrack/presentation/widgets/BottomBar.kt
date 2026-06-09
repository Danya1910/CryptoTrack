package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Lavender
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Purple


@Composable
fun BottomBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavComponents.Market,
        BottomNavComponents.Purchase,
        BottomNavComponents.Search,
        BottomNavComponents.Favorite,
        BottomNavComponents.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    Box(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .padding(horizontal = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navController.navigate(item.route)
                        }
                ) {
                    BottomItem(
                        path = item.icon,
                        title = item.title,
                        isSelected = isSelected
                    )
                }
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
        BottomNavComponents.Favorite,
        BottomNavComponents.Profile
    )


    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items.forEach { item ->
                BottomItem(
                    path = item.icon,
                    title = item.title,
                    isSelected = false,
                )
            }
        }
    }

//    Surface(
//        color = BlackNavigation,
//        modifier = Modifier
//            .shadow(
//                elevation = 2.dp,
//                spotColor = Color.White,
//            )
//            .height(65.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            items.forEach { item ->
//                Icon(
//                    painter = painterResource(item.icon),
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.size(32.dp)
//                )
//            }
//        }
//    }
//    Text(
//        text = "Preview!!!",
//        fontSize = 15.sp,
//        color = Color.White
//    )
}

@Composable
private fun BottomItem(
    path: Int,
    title: String,
    isSelected: Boolean,
) {

    val tint = if(isSelected) Lavender else Color.Gray

    val textColor = if(isSelected) Lavender else Color.Gray

    val backgroundColor = if(isSelected) Purple else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(55.dp)
                .height(30.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Icon(
                painter = painterResource(path),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(25.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = textColor,
        )
    }

}