package com.santansarah.city_kmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform