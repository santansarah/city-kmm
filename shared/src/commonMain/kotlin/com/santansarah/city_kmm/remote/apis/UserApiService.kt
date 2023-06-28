package com.santansarah.city_kmm.remote.apis

import com.santansarah.city_kmm.remote.models.UserApiResponse
import com.santansarah.city_kmm.utils.ServiceResult
import com.santansarah.city_kmm.utils.toCityApiError
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserApiService : KtorApi() {

    companion object {
        val USER_BY_JWT = "$BASE_URL/users/authenticate"
        val USER_BY_ID = "$BASE_URL/users"
    }

    suspend fun getUser(
        nonce: String,
        jwtToken: String
    ): ServiceResult<UserApiResponse> {

        return try {
            val userApiResponse: UserApiResponse = client.get(USER_BY_JWT)
            {
                bearerAuth(jwtToken)
                headers {
                    append("x-nonce", nonce)
                }
                contentType(ContentType.Application.Json)
            }.body()
            ServiceResult.Success(userApiResponse)
        } catch (apiError: Exception) {

            val parsedError = apiError.toCityApiError<UserApiResponse>()
            parsedError

        }
    }

    suspend fun getUser(id: Int): ServiceResult<UserApiResponse> {

        return try {
                val userApiResponse: UserApiResponse = client.get(USER_BY_ID) {
                    headers {
                        append("x-api-key", APP_API_KEY)
                    }
                    url {
                        appendPathSegments(id.toString())
                    }
                }.body()

            ServiceResult.Success(userApiResponse)

        } catch (apiError: Exception) {
            val parsedError = apiError.toCityApiError<UserApiResponse>()
            parsedError
        }
    }
}
