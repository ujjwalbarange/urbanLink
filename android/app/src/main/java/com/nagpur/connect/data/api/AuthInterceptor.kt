package com.nagpur.connect.data.api

import com.nagpur.connect.data.repository.CitizenIdentityManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val identityManager: CitizenIdentityManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val guestId = identityManager.getOrCreateGuestId()

        val request = original.newBuilder()
            .header("x-guest-id", guestId)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}
