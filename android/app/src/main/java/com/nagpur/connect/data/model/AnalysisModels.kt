package com.nagpur.connect.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeIncidentRequest(
    val text: String,
    val locationContext: String? = null,
    val selectedDepartment: String? = null
)

@Serializable
data class AnalyzeIncidentResponse(
    val success: Boolean = false,
    val analysis: AnalysisResultModel? = null,
    val error: ErrorDetails? = null
)

@Serializable
data class ErrorDetails(
    val message: String? = null
)

@Serializable
data class AnalysisResultModel(
    val mainCategory: String = "general",
    val mainCategoryName: String = "General Civic",
    val subcategory: String = "general",
    val incidentType: String = "civic_issue",
    val title: String = "Civic Incident",
    val citizenSummary: String = "",
    val summary: String = "",
    val severity: SeverityInfoModel = SeverityInfoModel(),
    val priority: PriorityInfoModel = PriorityInfoModel(),
    val confidence: ConfidenceInfoModel = ConfidenceInfoModel(),
    val departments: List<NotifiedDeptModel> = emptyList(),
    val mismatch: Boolean = false,
    val mismatchReason: String? = null,
    val suggestedCategory: String? = null,
    val suggestedCategoryName: String? = null,
    val deptQuestions: List<DeptQuestionModel> = emptyList(),
    val location: AnalysisLocationModel = AnalysisLocationModel(),
    val privacy: PrivacyModel = PrivacyModel(),
    val isEmergency: Boolean = false,
    val affectedPeople: String? = null,
    val aiModel: String = "Llama-3.3-70B",
    val processingTimeMs: Long = 650
)

@Serializable
data class SeverityInfoModel(
    val level: String = "medium", // critical, high, medium, low
    val score: Int = 50,
    val reason: String = "Standard priority civic report."
)

@Serializable
data class PriorityInfoModel(
    val score: Int = 50,
    val band: String = "STANDARD"
)

@Serializable
data class ConfidenceInfoModel(
    val overall: Double = 0.90
)

@Serializable
data class NotifiedDeptModel(
    val code: String,
    val name: String,
    val isPrimary: Boolean = false,
    val reason: String = "Primary jurisdiction for resolving this report."
)

@Serializable
data class DeptQuestionModel(
    val id: String,
    val question: String,
    val type: String = "chip", // "chip", "multi_chip", "yesno", "text"
    val options: List<String>? = null,
    val placeholder: String? = null,
    val required: Boolean = false
)

@Serializable
data class AnalysisLocationModel(
    val text: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
)

@Serializable
data class PrivacyModel(
    val level: String = "normal",
    val protectIdentity: Boolean = false
)

@Serializable
data class FinalizeIncidentRequest(
    val originalText: String,
    val analysis: AnalysisResultModel,
    val answers: Map<String, String>,
    val locationText: String? = null
)

@Serializable
data class FinalizeIncidentResponse(
    val success: Boolean = false,
    val finalReport: GroqFinalReportModel? = null
)

@Serializable
data class GroqFinalReportModel(
    val finalSummary: String = "",
    val severity: String = "medium",
    val priorityScore: Int = 50,
    val affectedPeople: String? = null,
    val keyFindings: List<String> = emptyList(),
    val recommendedActions: List<String> = emptyList()
)
