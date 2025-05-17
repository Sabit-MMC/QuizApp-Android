package com.example.quizapp.tools

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "quiz_user_prefs")

@Singleton
class DataStoreHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val PASSWORD_KEY = stringPreferencesKey("password")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveCredentials(userId: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
            prefs[PASSWORD_KEY] = password
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }

    fun getUserId(): Flow<String?> = context.dataStore.data.map { it[USER_ID_KEY] }

    fun getPassword(): Flow<String?> = context.dataStore.data.map { it[PASSWORD_KEY] }
    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }
    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }

    suspend fun clearCredentials() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun clearAccessToken() {
        context.dataStore.edit { it.remove(ACCESS_TOKEN) }
    }

    suspend fun clearRefreshToken() {
        context.dataStore.edit { it.remove(REFRESH_TOKEN) }
    }
}