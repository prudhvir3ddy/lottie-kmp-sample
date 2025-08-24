package kottieComposition

import androidx.compose.runtime.Composable
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
actual fun rememberKottieComposition(
    spec: KottieCompositionSpec
): Any? {
    return kottieComposition(spec = spec)
}