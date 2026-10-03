package com.nagpur.connect.data.repository

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class CitizenIdentityManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "nagpur_connect_citizen_identity",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_GUEST_ID = "guest_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"

        @Volatile
        private var INSTANCE: CitizenIdentityManager? = null

        fun getInstance(context: Context): CitizenIdentityManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CitizenIdentityManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    /**
     * Retrieves existing guest ID or creates a new stable UUID v4 and persists it.
     */
    fun getOrCreateGuestId(): String {
        var guestId = prefs.getString(KEY_GUEST_ID, null)
        if (guestId.isNullOrBlank()) {
            guestId = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_GUEST_ID, guestId).apply()
        }
        return guestId
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "Citizen") ?: "Citizen"
    }

    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "Nagpur, Maharashtra") ?: "Nagpur, Maharashtra"
    }

    fun setProfile(name: String, email: String) {
        prefs.edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }
}
