package com.huntermobile.app.core

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object AuthManager {
    private const val PREFS_NAME = "hunter_prefs"
    private const val KEY_TOKEN = "auth_token"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        if (!::preferences.isInitialized) {
            preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    var token: String?
        get() = if (::preferences.isInitialized) preferences.getString(KEY_TOKEN, null) else null
        set(value) {
            if (::preferences.isInitialized) {
                preferences.edit { putString(KEY_TOKEN, value) }
            }
        }

    fun clear() {
        if (::preferences.isInitialized) {
            preferences.edit { remove(KEY_TOKEN) }
        }
    }
}
