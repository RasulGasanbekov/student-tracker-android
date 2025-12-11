package com.edutrack.data.storage

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class SharedPrefsTokenStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenStorage {

    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    override suspend fun saveToken(token: String) {
        sharedPreferences.edit { putString(TOKEN_KEY, token) }
    }

    override suspend fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    override suspend fun clearToken() {
        sharedPreferences.edit { remove(TOKEN_KEY) }
    }
}