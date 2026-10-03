package com.nagpur.connect.data.api

import com.nagpur.connect.data.model.ActiveReportsResponse
import com.nagpur.connect.data.model.AnalyzeIncidentRequest
import com.nagpur.connect.data.model.AnalyzeIncidentResponse
import com.nagpur.connect.data.model.CreateIncidentRequest
import com.nagpur.connect.data.model.CreateIncidentResponse
import com.nagpur.connect.data.model.FinalizeIncidentRequest
import com.nagpur.connect.data.model.FinalizeIncidentResponse
import com.nagpur.connect.data.model.RegisterGuestResponse
import com.nagpur.connect.data.model.TrackingIncidentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NagpurConnectApi {

    @POST("api/citizen/register-guest")
    suspend fun registerGuest(): Response<RegisterGuestResponse>

    @POST("api/incidents/analyze")
    suspend fun analyzeIncident(
        @Body request: AnalyzeIncidentRequest
    ): Response<AnalyzeIncidentResponse>

    @POST("api/incidents/finalize")
    suspend fun finalizeIncident(
        @Body request: FinalizeIncidentRequest
    ): Response<FinalizeIncidentResponse>

    @POST("api/incidents/create")
    suspend fun createIncident(
        @Body request: CreateIncidentRequest
    ): Response<CreateIncidentResponse>

    @GET("api/incidents/mine")
    suspend fun getMyIncidents(): Response<ActiveReportsResponse>

    @GET("api/incidents/{reference}")
    suspend fun getIncidentByReference(
        @Path("reference") reference: String
    ): Response<TrackingIncidentResponse>
}
