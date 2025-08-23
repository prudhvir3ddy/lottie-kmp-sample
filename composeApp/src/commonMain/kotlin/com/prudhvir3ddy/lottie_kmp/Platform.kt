package com.prudhvir3ddy.lottie_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform