import UIKit
import Foundation
import Lottie

@objc public class LottieHelper: NSObject {
    private static var animationViews: [UIView: LottieAnimationView] = [:]
    
    @objc public static func createLottieView() -> UIView {
        let containerView = UIView()
        containerView.backgroundColor = UIColor.clear
        return containerView
    }
    
    @objc public static func loadAnimation(view: UIView, url: String, iterations: Int) {
        // Remove any existing animation view
        if let existingAnimationView = animationViews[view] {
            existingAnimationView.removeFromSuperview()
            animationViews.removeValue(forKey: view)
        }
        
        // Create new animation view
        let animationView = LottieAnimationView()
        animationView.translatesAutoresizingMaskIntoConstraints = false
        animationView.contentMode = .scaleAspectFit
        animationView.backgroundBehavior = .pauseAndRestore
        
        // Store reference
        animationViews[view] = animationView
        
        // Add to container
        view.addSubview(animationView)
        NSLayoutConstraint.activate([
            animationView.topAnchor.constraint(equalTo: view.topAnchor),
            animationView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            animationView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            animationView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        // Load animation from URL
        guard let animationURL = URL(string: url) else {
            showError(in: view, message: "Invalid URL")
            return
        }
        
        // Download and load animation
        downloadAndLoadAnimation(animationView: animationView, url: animationURL, iterations: iterations, containerView: view)
    }
    
    private static func downloadAndLoadAnimation(animationView: LottieAnimationView, url: URL, iterations: Int, containerView: UIView) {
        URLSession.shared.dataTask(with: url) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    showError(in: containerView, message: "Network error: \(error.localizedDescription)")
                    return
                }
                
                guard let data = data else {
                    showError(in: containerView, message: "No data received")
                    return
                }
                
                do {
                    // Parse JSON
                    let jsonObject = try JSONSerialization.jsonObject(with: data, options: [])
                    let jsonData = try JSONSerialization.data(withJSONObject: jsonObject, options: [])
                    
                    // Create animation from JSON data
                    guard let animation = try? LottieAnimation.from(data: jsonData) else {
                        showError(in: containerView, message: "Invalid Lottie JSON")
                        return
                    }
                    
                    // Configure animation
                    animationView.animation = animation
                    
                    // Set loop mode based on iterations
                    if iterations == -1 {
                        animationView.loopMode = .loop
                    } else if iterations == 1 {
                        animationView.loopMode = .playOnce
                    } else {
                        animationView.loopMode = .repeat(Float(iterations))
                    }
                    
                    // Start animation
                    animationView.play()
                    
                } catch {
                    showError(in: containerView, message: "JSON parsing error: \(error.localizedDescription)")
                }
            }
        }.resume()
    }
    
    private static func showError(in view: UIView, message: String) {
        // Remove any existing error label
        view.subviews.forEach { subview in
            if subview.tag == 999 {
                subview.removeFromSuperview()
            }
        }
        
        let errorLabel = UILabel()
        errorLabel.tag = 999
        errorLabel.text = "Error: \(message)"
        errorLabel.numberOfLines = 0
        errorLabel.textAlignment = .center
        errorLabel.textColor = .red
        errorLabel.backgroundColor = UIColor.lightGray.withAlphaComponent(0.8)
        errorLabel.translatesAutoresizingMaskIntoConstraints = false
        
        view.addSubview(errorLabel)
        NSLayoutConstraint.activate([
            errorLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            errorLabel.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            errorLabel.leadingAnchor.constraint(greaterThanOrEqualTo: view.leadingAnchor, constant: 8),
            errorLabel.trailingAnchor.constraint(lessThanOrEqualTo: view.trailingAnchor, constant: -8)
        ])
    }
    
    @objc public static func disposeAnimation(view: UIView) {
        if let animationView = animationViews[view] {
            animationView.stop()
            animationView.removeFromSuperview()
            animationViews.removeValue(forKey: view)
        }
    }
}