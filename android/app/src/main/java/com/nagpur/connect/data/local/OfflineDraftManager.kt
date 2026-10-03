package com.nagpur.connect.data.local

import android.content.Context
import android.content.SharedPreferences

class OfflineDraftManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "nagpur_connect_offline_drafts",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_DRAFT_TEXT = "draft_text"
        private const val KEY_DRAFT_CATEGORY = "draft_category"
        private const val KEY_DRAFT_LOCATION = "draft_location"

        @Volatile
        private var INSTANCE: OfflineDraftManager? = null

        fun getInstance(context: Context): OfflineDraftManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OfflineDraftManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun saveDraft(text: String, category: String?, location: String?) {
        prefs.edit()
            .putString(KEY_DRAFT_TEXT, text)
            .putString(KEY_DRAFT_CATEGORY, category)
            .putString(KEY_DRAFT_LOCATION, location)
            .apply()
    }

    fun getSavedText(): String = prefs.getString(KEY_DRAFT_TEXT, "") ?: ""
    fun getSavedCategory(): String? = prefs.getString(KEY_DRAFT_CATEGORY, null)
    fun getSavedLocation(): String? = prefs.getString(KEY_DRAFT_LOCATION, null)

    fun clearDraft() {
        prefs.edit().clear().apply()
    }

    fun hasDraft(): Boolean = getSavedText().isNotBlank()
}
