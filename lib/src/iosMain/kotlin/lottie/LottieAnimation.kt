package lottie

import cocoapods.lottie_ios.CompatibleAnimationView
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import contentScale.ContentScale
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.UIViewContentMode


@OptIn(ExperimentalForeignApi::class)
@Composable
fun LottieAnimation(
    modifier: Modifier,
    composition: Any?,
    progress: () -> Float,
    backgroundColor: Color,
    contentScale: ContentScale,
    clipToCompositionBounds: Boolean
) {
    when (composition as? CompatibleAnimationView) {
        null -> {}
        else -> {

            val scaleType = when(contentScale){
                ContentScale.Fit -> UIViewContentMode.UIViewContentModeScaleAspectFit
                ContentScale.Crop -> UIViewContentMode.UIViewContentModeScaleAspectFill
                ContentScale.FillBounds -> UIViewContentMode.UIViewContentModeScaleToFill
            }

            androidx.compose.ui.viewinterop.UIKitView(
                factory = {
                    UIView().apply {
                        this.backgroundColor = UIColor.clearColor
                        this.tintColor = UIColor.clearColor
                        this.clipsToBounds = clipToCompositionBounds
                    }
                },
                modifier = modifier.background(backgroundColor),
                update = { view ->

                    composition.translatesAutoresizingMaskIntoConstraints = false
                    composition.contentMode = scaleType
                    composition.clipsToBounds = clipToCompositionBounds

                    // Only add subview if it's not already added
                    if (composition.superview != view) {
                        // Remove from previous parent if exists
                        composition.removeFromSuperview()
                        view.addSubview(composition)
                        
                        NSLayoutConstraint.activateConstraints(
                            listOf(
                                composition.widthAnchor.constraintEqualToAnchor(view.widthAnchor),
                                composition.heightAnchor.constraintEqualToAnchor(view.heightAnchor)
                            )
                        )
                    }

                }
            )

        }
    }
}

