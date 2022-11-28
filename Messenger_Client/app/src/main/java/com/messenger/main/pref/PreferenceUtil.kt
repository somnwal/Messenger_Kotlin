package com.messenger.main.pref

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefsFilename = "prefs"

    private val prefs: SharedPreferences =
        context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)

    var user: String
        get() = prefs.getString("user", "") ?: ""
        set(value) = prefs.edit().putString("user", value).apply()

    var userName: String
        get() = prefs.getString("user_name", "") ?: ""
        set(value) = prefs.edit().putString("user_name", value).apply()

    var token: String
        get() = prefs.getString("token", "") ?: ""
        set(value) = prefs.edit().putString("token", value).apply()



    fun remove(key: String) {
        prefs.edit().putString(key, null).apply()
    }
}