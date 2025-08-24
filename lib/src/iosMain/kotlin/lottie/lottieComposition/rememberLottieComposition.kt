package lottie.lottieComposition

import cocoapods.lottie_ios.CompatibleAnimation
import cocoapods.lottie_ios.CompatibleAnimationView
import cocoapods.lottie_ios.CompatibleRenderingEngineOptionAutomatic
import cocoapods.lottie_ios.CompatibleRenderingEngineOptionCoreAnimation
import cocoapods.lottie_ios.CompatibleRenderingEngineOptionDefaultEngine
import cocoapods.lottie_ios.CompatibleRenderingEngineOptionMainThread
import cocoapods.lottie_ios.CompatibleRenderingEngineOptionShared
import cocoapods.lottie_ios.LottieAnimationView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIColor


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.toULong())
}


@OptIn(ExperimentalForeignApi::class)
@Composable
internal fun rememberLottieComposition(
    spec: LottieCompositionSpec
): CompatibleAnimationView? {

    var animationState by remember(spec) {
        mutableStateOf<CompatibleAnimationView?>(null)
    }

    LaunchedEffect(spec) {
        val animation = when (spec) {
            is LottieCompositionSpec.File -> {
                CompatibleAnimationView(
                    data = spec.jsonString.encodeToByteArray().toNSData()
                )
            }

            is LottieCompositionSpec.Url -> {
                val httpClient = HttpClient()
                val data = httpClient.get(spec.url)
                CompatibleAnimationView(
                    data = data.readBytes().toNSData()
                )
            }

            is LottieCompositionSpec.JsonString -> {
                CompatibleAnimationView(
                    data = spec.jsonString.encodeToByteArray().toNSData()
                )
            }

        }
        animationState = animation
    }

    return animationState

}

