package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.cryptotrack.presentation.util.something.shimmer


@Composable
fun SkeletonBox(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(4.dp))
            .shimmer()
    )
}