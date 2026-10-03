package com.nagpur.connect.data.repository

import android.content.Context
import com.nagpur.connect.data.api.NagpurConnectApi
import com.nagpur.connect.data.api.NetworkClient
import com.nagpur.connect.data.model.ActiveReportModel
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.data.model.AnalyzeIncidentRequest
import com.nagpur.connect.data.model.CreateIncidentRequest
import com.nagpur.connect.data.model.CreateIncidentResponse
import com.nagpur.connect.data.model.FinalizeIncidentRequest
import com.nagpur.connect.data.model.GroqFinalReportModel
import com.nagpur.connect.data.model.TrackingIncidentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncidentRepository(context: Context) {
    private val api: NagpurConnectApi = NetworkClient.getApi(context)

    suspend fun analyzeIncident(
        text: String,
        locationContext: String?,
        selectedDepartment: String?
    ): Result<AnalysisResultModel> = withContext(Dispatchers.IO) {
        try {
            val response = api.analyzeIncident(
                AnalyzeIncidentRequest(
                    text = text,
                    locationContext = locationContext,
                    selectedDepartment = selectedDepartment
                )
            )
            if (response.isSuccessful && response.body()?.success == true && response.body()?.analysis != null) {
                Result.success(response.body()!!.analysis!!)
            } else {
                val errorMsg = response.body()?.error?.message 
                    ?: "Analysis failed (${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finalizeIncident(
        originalText: String,
        analysis: AnalysisResultModel,
        answers: Map<String, String>,
        locationText: String?
    ): Result<GroqFinalReportModel?> = withContext(Dispatchers.IO) {
        try {
            val response = api.finalizeIncident(
                FinalizeIncidentRequest(
                    originalText = originalText,
                    analysis = analysis,
                    answers = answers,
                    locationText = locationText
                )
            )
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.finalReport)
            } else {
                Result.success(null) // Non-fatal, fallback to base analysis
            }
        } catch (e: Exception) {
            Result.success(null)
        }
    }

    suspend fun createIncident(
        request: CreateIncidentRequest
    ): Result<CreateIncidentResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.createIncident(request)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.incident != null) {
                Result.success(body)
            } else {
                val msg = body?.error ?: "Failed to submit report (${response.code()})"
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyIncidents(): Result<List<ActiveReportModel>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMyIncidents()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.incidents)
            } else {
                Result.failure(Exception("Failed to fetch reports (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getIncidentByReference(
        reference: String
    ): Result<TrackingIncidentResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.getIncidentByReference(reference)
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.incident != null) {
                Result.success(body)
            } else {
                Result.failure(Exception(body?.error ?: "Report not found (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
