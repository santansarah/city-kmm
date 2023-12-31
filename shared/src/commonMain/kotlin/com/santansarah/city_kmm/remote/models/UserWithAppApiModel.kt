package com.santansarah.city_kmm.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWithAppApiModel(
    val userId: Int = 0,
    val email: String = "",
    val name: String = "",
    val userCreateDate: String = "",
    val userAppId: Int = 0,
    val appName: String = "",
    val appType: AppType = AppType.DEVELOPMENT,
    val apiKey: String = "",
    val appCreateDate: String = ""
)

@Serializable
enum class AppType {
    NOTSET,
    @SerialName("dev")
    DEVELOPMENT,
    @SerialName("prod")
    PRODUCTION;

    companion object {
        fun toSelectList() = values().filterNot {
            it == NOTSET
        }

    }
}


