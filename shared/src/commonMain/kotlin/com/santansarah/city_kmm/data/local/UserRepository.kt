package com.santansarah.city_kmm.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import co.touchlab.kermit.Logger
import com.santansarah.city_kmm.remote.apis.UserApiService
import com.santansarah.city_kmm.utils.Dispatcher
import com.santansarah.city_kmm.utils.ErrorCode
import com.santansarah.city_kmm.utils.ServiceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent


data class UserPreferences(
    val lastOnboardingScreen: Int = 0,
    val isOnboardingComplete: Boolean = false,
    val userId: Int = 0,
    val isSignedOut: Boolean
)

class UserRepository(
    private val dataStore: DataStore<Preferences>,
    private val userApiService: UserApiService,
    private val dispatcher: Dispatcher
) : KoinComponent {

    private val logger = Logger.withTag("UserPreferencesManager")

    private object PreferencesKeys {
        val LAST_ONBOARDING_SCREEN = intPreferencesKey("last_onboarding")
        val USER_ID = intPreferencesKey("userId")
        val IS_SIGNED_OUT = booleanPreferencesKey("isSignedOut")
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    /**
     * Use this if you don't want to observe a flow.
     */
    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    /**
     * Get the user preferences flow. When it's collected, keys are mapped to the
     * [UserPreferences] data class.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                logger.d { "Error reading preferences: $exception" }
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    val currentUserFlow: Flow<CurrentUser> = dataStore.data
        .catch {
            CurrentUser.UnknownSignIn
        }.map { preferences ->
            mapUserPreferences(preferences)
        }.map {
            logger.d { "collecting CurrentUser: $it" }
            if (it.userId <= 0)
                CurrentUser.UnknownSignIn
            else {
                if (it.isSignedOut)
                    CurrentUser.SignedOutUser
                else {
                    getUser(it.userId)
                }
            }
        }.flowOn(dispatcher.io)


    /**
     * Sets the last onboarding screen that was viewed (on button click).
     */
    suspend fun setLastOnboardingScreen(viewedScreen: Int) {
        // updateData handles data transactionally, ensuring that if the key is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] = viewedScreen
        }
    }

    /**
     * Sets the userId that we get from the Ktor API (on button click).
     */
    suspend fun setUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun isSignedOut(isSignedOut: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SIGNED_OUT] = isSignedOut
        }
    }

    /**
     * Get the preferences key, then map it to the data class.
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val lastScreen = preferences[PreferencesKeys.LAST_ONBOARDING_SCREEN] ?: 0
        val isOnBoardingComplete: Boolean = (lastScreen >= 2)
        val userId = preferences[PreferencesKeys.USER_ID] ?: 0
        val isSignedOut = preferences[PreferencesKeys.IS_SIGNED_OUT] ?: false

        return UserPreferences(lastScreen, isOnBoardingComplete, userId, isSignedOut)
    }

    suspend fun getUser(nonce: String, jwtToken: String): ServiceResult<CurrentUser> =
        when (val insertResult = userApiService.getUser(nonce,jwtToken)) {
            is ServiceResult.Success -> {
                isSignedOut(false)
                with(insertResult.data.user) {
                    setUserId(userId)
                    ServiceResult.Success(
                        CurrentUser.SignedInUser(
                            userId = userId,
                            name = name,
                            email = email
                        )
                    )
                }
            }
            is ServiceResult.Error -> insertResult
        }

    suspend fun getUser(userId: Int): CurrentUser = withContext(dispatcher.io) {
        return@withContext when (val getUserResult = userApiService.getUser(userId)) {
            is ServiceResult.Success -> {
                isSignedOut(false)
                with(getUserResult.data.user) {
                    CurrentUser.SignedInUser(
                        userId = userId,
                        email = email,
                        name = name
                    )
                }
            }

            is ServiceResult.Error -> {
                logger.d { "getuser: ${getUserResult.message}"}
                CurrentUser.NotAuthenticated(
                    userId,
                    ErrorCode.SIGNIN_ERROR)
            }
        }
    }
}