package com.prudhvir3ddy.lottie_kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun LottieView(
    url: String,
    modifier: Modifier = Modifier,
    iterations: Int = 1
)