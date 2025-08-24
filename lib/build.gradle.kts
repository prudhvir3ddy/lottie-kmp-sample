plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0"
        ios.deploymentTarget = "17.4"
        pod("lottie-ios") {
            moduleName = "Lottie"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

//    iosArm64 {
//        compilations.getByName("main") {
//            val Lottie by cinterops.creating {
//                defFile("src/nativeInterop/cinterop/Lottie.def")
//                val path = "$rootDir/vendor/Lottie.xcframework/ios-arm64"
//                compilerOpts("-F$path", "-framework", "Lottie", "-rpath", path)
//                extraOpts += listOf("-compiler-option", "-fmodules")
//            }
//        }
//    }
//
//    listOf(
//        iosX64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.compilations.getByName("main") {
//            val Lottie by cinterops.creating {
//                defFile("src/nativeInterop/cinterop/Lottie.def")
//                val path = "$rootDir/vendor/Lottie.xcframework/ios-arm64_x86_64-simulator"
//                compilerOpts("-F$path", "-framework", "Lottie", "-rpath", path)
//                extraOpts += listOf("-compiler-option", "-fmodules")
//            }
//        }
//    }


    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.androidx.activity.compose)
                implementation(libs.lottie.android)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.darwin)
            }
        }

    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


