package com.santansarah.city_kmm.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class CityApiModel(
    val zip: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val city: String = "",
    val state: String = "",
    val population: Int = 0,
    val county: String = ""
)