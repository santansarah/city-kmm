package com.santansarah.city_kmm.utils

import co.touchlab.kermit.Logger
import com.santansarah.city_kmm.remote.models.CityApiResponse
import com.santansarah.city_kmm.remote.models.ResponseErrors
import com.santansarah.city_kmm.remote.models.UserApiResponse
import com.santansarah.city_kmm.remote.models.UserWithAppApiResponse
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.json.Json

/**
 * This function takes the ktor Response Error: e.message value
 * and extracts the cachedResponseText value, which in my case is JSON.
 * It removes a leading and trailing quote.
 */

val logger = Logger.withTag("ExtensionFunctions")

inline fun <reified T> String.toErrorResponse(): T {
    val errorResponse = this.substringAfter("Text: \"").dropLast(1)
    logger.d {"errorResponse parsed: $errorResponse"}
    return Json.decodeFromString(errorResponse)
}

/**
 * Try to return the error that came back from the ktor API;
 * otherwise, just return a generic network error.
 */
inline fun <reified T> Exception.toCityApiError(): ServiceResult.Error {

    val code = "API_ERROR"
    val message = message ?: "Network error."

    return try {
        when (this) {
            is CancellationException -> ServiceResult.Error(
                ErrorCode.JOB_CANCELLED.name,
                ErrorCode.JOB_CANCELLED.message
            )
            is ServerResponseException, is ClientRequestException -> {
                val errors = when (val cityApiResponse: T = message.toErrorResponse()) {
                    is CityApiResponse -> {
                        cityApiResponse.errors.firstOrNull() ?: ResponseErrors(code, message)
                    }
                    is UserApiResponse -> {
                        cityApiResponse.errors.firstOrNull() ?: ResponseErrors(code, message)
                    }
                    is UserWithAppApiResponse -> {
                        logger.d { "appResponse: $cityApiResponse" }
                        cityApiResponse.errors.firstOrNull() ?: ResponseErrors(code, message)
                    }
                    else -> {
                        ResponseErrors(code, message)
                    }
                }
                ServiceResult.Error(errors!!.code, errors.message)
            }
            else -> {
                logger.d { "not a ktor exception. $this"}
                ServiceResult.Error(code, message)
            }
        }
    } catch (parseError: Exception) {
        logger.d { "parse error: $parseError"}
        ServiceResult.Error(code, message)
    }
}

