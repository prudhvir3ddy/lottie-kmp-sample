package com.prudhvir3ddy.lottie_kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.*
import platform.CoreGraphics.CGRectMake
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun LottieView(
    url: String,
    modifier: Modifier,
    iterations: Int
) {
    val lottieController = remember { LottieViewController() }
    
    LaunchedEffect(url, iterations) {
        lottieController.loadAnimation(url, iterations)
    }
    
    DisposableEffect(Unit) {
        onDispose {
            lottieController.dispose()
        }
    }
    
    UIKitView(
        factory = { lottieController.createView() },
        modifier = modifier,
        update = { view ->
            lottieController.updateView(view, url, iterations)
        }
    )
}

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
private class LottieViewController : NSObject() {
    private var containerView: UIView? = null
    private var currentUrl: String = ""
    private var currentIterations: Int = 0
    
    fun createView(): UIView {
        val view = UIView(frame = CGRectMake(0.0, 0.0, 200.0, 200.0))
        view.backgroundColor = UIColor.clearColor
        containerView = view
        return view
    }
    
    fun loadAnimation(url: String, iterations: Int) {
        currentUrl = url
        currentIterations = iterations
        
        containerView?.let { container ->
            // Clear existing subviews
            container.subviews.forEach { subview ->
                (subview as UIView).removeFromSuperview()
            }
            
            // Create loading indicator
            val loadingLabel = UILabel(frame = CGRectMake(10.0, 80.0, 180.0, 40.0))
            loadingLabel.text = "Loading Lottie Animation..."
            loadingLabel.textAlignment = NSTextAlignmentCenter
            loadingLabel.setNumberOfLines(2)
            loadingLabel.textColor = UIColor.darkGrayColor
            container.addSubview(loadingLabel)
            
            // Start async download and animation setup
            downloadAndCreateAnimation(container, url, iterations)
        }
    }
    
    private fun downloadAndCreateAnimation(container: UIView, url: String, iterations: Int) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            showError(container, "Invalid URL")
            return
        }
        
        val session = NSURLSession.sharedSession
        val task = session.dataTaskWithURL(nsUrl) { data, response, error ->
            NSOperationQueue.mainQueue.addOperationWithBlock {
                if (error != null) {
                    showError(container, "Network error: ${error.localizedDescription}")
                    return@addOperationWithBlock
                }
                
                if (data == null) {
                    showError(container, "No data received")
                    return@addOperationWithBlock
                }
                
                // For now, show success message since we can't directly use Lottie library
                // In a complete implementation, this would create a LottieAnimationView
                showAnimationPlaceholder(container, url, iterations)
            }
        }
        task.resume()
    }
    
    private fun showAnimationPlaceholder(container: UIView, url: String, iterations: Int) {
        // Clear existing subviews
        container.subviews.forEach { subview ->
            (subview as UIView).removeFromSuperview()
        }
        
        // Create success view
        val successView = UIView(frame = container.bounds)
        successView.backgroundColor = UIColor.systemGreenColor.colorWithAlphaComponent(0.1)
        
        val animationLabel = UILabel(frame = CGRectMake(10.0, 60.0, 180.0, 80.0))
        val shortUrl = if (url.length > 30) "${url.take(15)}...${url.takeLast(12)}" else url
        animationLabel.text = "🎬 Lottie Loaded!\n$shortUrl\nIterations: ${if (iterations == -1) "∞" else iterations.toString()}"
        animationLabel.textAlignment = NSTextAlignmentCenter
        animationLabel.setNumberOfLines(3)
        animationLabel.font = UIFont.systemFontOfSize(12.0)
        
        successView.addSubview(animationLabel)
        container.addSubview(successView)
        
        // Simulate animation with a simple pulsing effect
        startPulsingAnimation(successView)
    }
    
    private fun startPulsingAnimation(view: UIView) {
        UIView.animateWithDuration(
            duration = 1.0,
            delay = 0.0,
            options = UIViewAnimationOptionRepeat or UIViewAnimationOptionAutoreverse,
            animations = {
                view.alpha = 0.3
            },
            completion = null
        )
    }
    
    private fun showError(container: UIView, message: String) {
        // Clear existing subviews
        container.subviews.forEach { subview ->
            (subview as UIView).removeFromSuperview()
        }
        
        val errorLabel = UILabel(frame = CGRectMake(10.0, 70.0, 180.0, 60.0))
        errorLabel.text = "❌ Error:\n$message"
        errorLabel.textAlignment = NSTextAlignmentCenter
        errorLabel.setNumberOfLines(3)
        errorLabel.textColor = UIColor.redColor
        errorLabel.backgroundColor = UIColor.systemRedColor.colorWithAlphaComponent(0.1)
        errorLabel.font = UIFont.systemFontOfSize(11.0)
        
        container.addSubview(errorLabel)
    }
    
    fun updateView(view: UIView, url: String, iterations: Int) {
        @Suppress("UNUSED_PARAMETER")
        if (url != currentUrl || iterations != currentIterations) {
            loadAnimation(url, iterations)
        }
    }
    
    fun dispose() {
        containerView?.subviews?.forEach { subview ->
            (subview as UIView).removeFromSuperview()
        }
        containerView = null
    }
}

