package com.santansarah.city_kmm.remote.apis

import com.santansarah.city_kmm.BuildKonfig
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

abstract class KtorApi {

    companion object {
        val BASE_URL = "http://${BuildKonfig.KTOR_IP_ADDR}:8080"
        const val APP_API_KEY = "Pr67HTHS4VIP1eN"
    }

    val client = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
}

