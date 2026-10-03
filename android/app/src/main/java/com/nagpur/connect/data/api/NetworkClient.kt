package com.nagpur.connect.data.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nagpur.connect.data.repository.CitizenIdentityManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkClient {

    // Default base URL pointing to live backend (can also be switched to http://10.0.2.2:3000/ for local dev)
    private var baseUrl: String = "https://nagpur-connect.vercel.app/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        encodeDefaults = true
    }

    private var apiInstance: NagpurConnectApi? = null

    fun setBaseUrl(url: String) {
        baseUrl = if (url.endsWith("/")) url else "$url/"
        apiInstance = null // Invalidate cached instance to re-initialize
    }

    fun getBaseUrl(): String = baseUrl

    fun getApi(context: Context): NagpurConnectApi {
        return apiInstance ?: synchronized(this) {
            apiInstance ?: createApi(context).also { apiInstance = it }
        }
    }

    private fun createApi(context: Context): NagpurConnectApi {
        val identityManager = CitizenIdentityManager.getInstance(context)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(identityManager))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(NagpurConnectApi::class.java)
    }
}
