package com.prudhvir3ddy.lottie_kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
actual fun LottieView(
    url: String,
    modifier: Modifier,
    iterations: Int
) {
    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.Url(url)
    )
    
    LottieAnimation(
        composition = composition.value,
        modifier = modifier,
        iterations = if (iterations == -1) LottieConstants.IterateForever else iterations
    )
}