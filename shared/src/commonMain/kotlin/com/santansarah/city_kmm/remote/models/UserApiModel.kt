package com.santansarah.city_kmm.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val userId: Int = 0,
    val email: String = "",
    val name: String = "",
    val userCreateDate: String = "",
    val apps: List<UserWithAppApiModel> = emptyList()
)
