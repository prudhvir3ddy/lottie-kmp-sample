# Lottie KMP

A Kotlin Multiplatform project showcasing Lottie animation integration across Android and iOS platforms using Compose Multiplatform.

## Features

- 🎬 **Cross-platform Lottie animations** using expect/actual declarations
- 📱 **Android implementation** with lottie-compose library
- 🍎 **iOS implementation** with native Lottie-iOS integration
- 🔄 **URL-based animation loading** with async downloading
- ⚙️ **Configurable iterations** (single play, repeat count, infinite loops)
- 🎨 **Native UI integration** using UIKitView on iOS

## Project Structure

* [/composeApp](./composeApp/src) contains shared code across platforms:
    - [commonMain](./composeApp/src/commonMain/kotlin) - Common code and expect declarations
    - [androidMain](./composeApp/src/androidMain/kotlin) - Android actual implementations
    - [iosMain](./composeApp/src/iosMain/kotlin) - iOS actual implementations
    
* [/iosApp](./iosApp/iosApp) contains the iOS application entry point and platform-specific code

## Prerequisites

- **Android Studio** (latest stable)
- **Xcode** 15.0+ (for iOS development)
- **CocoaPods** (for iOS dependencies)
- **Kotlin Multiplatform** plugin

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd lottie-kmp
```

### 2. Android Setup

Android dependencies are handled automatically via Gradle. Simply:

```bash
./gradlew build
```

### 3. iOS Setup

#### Install CocoaPods (if not already installed):

```bash
# Install CocoaPods
sudo gem install cocoapods

# Verify installation
pod --version
```

#### Install iOS Dependencies:

```bash
cd iosApp
pod install
cd ..
```

**Important:** After running `pod install`, always use `iosApp.xcworkspace` instead of `iosApp.xcodeproj` when opening the project in Xcode.

### 4. Running the Project

#### Android:
- Open the project in Android Studio
- Run the Android configuration

#### iOS:
- Open `iosApp/iosApp.xcworkspace` in Xcode (not .xcodeproj!)
- Select a simulator or device
- Run the project

## Lottie Implementation

### Common Interface (Expect)
```kotlin
@Composable
expect fun LottieView(
    url: String,
    modifier: Modifier = Modifier,
    iterations: Int = 1
)
```

### Platform Implementations

#### Android (Actual)
Uses `lottie-compose` library with:
- `LottieAnimation` composable
- `rememberLottieComposition` for URL loading
- Native iteration control

#### iOS (Actual) 
Uses `UIKitView` integration with:
- Native iOS Lottie library via CocoaPods
- URL-based JSON downloading
- Native animation lifecycle management

## Usage Example

```kotlin
LottieView(
    url = "https://assets5.lottiefiles.com/packages/lf20_GoeyCV7pi2.json",
    modifier = Modifier.size(200.dp),
    iterations = -1 // Infinite loop
)
```

## Dependencies

### Android
- `lottie-compose` 6.6.2

### iOS
- `lottie-ios` 4.4.1 (via CocoaPods)

## Troubleshooting

### iOS Build Issues
1. **"Multiple commands produce .app"**: Clean build folder in Xcode (`Cmd+Shift+K`)
2. **Missing Lottie framework**: Run `pod install` in `iosApp` directory
3. **Can't find .xcworkspace**: Make sure to use `iosApp.xcworkspace`, not `.xcodeproj`

### General Issues
- Clean and rebuild: `./gradlew clean build`
- Invalidate caches in Android Studio: File → Invalidate Caches and Restart

## Learn More

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Lottie Android](https://github.com/airbnb/lottie-android)
- [Lottie iOS](https://github.com/airbnb/lottie-ios)