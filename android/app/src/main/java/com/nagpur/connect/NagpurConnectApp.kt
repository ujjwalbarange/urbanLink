package com.nagpur.connect

import android.app.Application
import com.nagpur.connect.data.repository.CitizenIdentityManager

class NagpurConnectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Ensure stable citizen UUID is generated and ready on app launch
        CitizenIdentityManager.getInstance(this).getOrCreateGuestId()
    }
}
