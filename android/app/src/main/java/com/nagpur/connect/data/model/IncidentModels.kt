package com.nagpur.connect.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AttachmentPayload(
    val fileName: String,
    val mimeType: String = "image/jpeg",
    val fileSize: Long = 0,
    val storageUrl: String,
    val purpose: String = "evidence"
)

@Serializable
data class AIConversationItemPayload(
    val questionId: String,
    val questionText: String,
    val questionType: String,
    val questionOptions: List<String>? = null,
    val answerValue: String = "",
    val required: Boolean = false,
    val sortOrder: Int = 0
)

@Serializable
data class CreateIncidentRequest(
    val originalText: String,
    val analysis: AnalysisResultModel? = null,
    val locationText: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val departmentAnswers: Map<String, String> = emptyMap(),
    val selectedDepartment: String? = null,
    val aiConversation: List<AIConversationItemPayload> = emptyList(),
    val finalReport: GroqFinalReportModel? = null,
    val attachments: List<AttachmentPayload> = emptyList()
)

@Serializable
data class CreateIncidentResponse(
    val success: Boolean = false,
    val incident: CreatedIncidentModel? = null,
    val geoRouting: GeoRoutingModel? = null,
    val error: String? = null
)

@Serializable
data class CreatedIncidentModel(
    val publicReference: String,
    val title: String,
    val severity: String = "MEDIUM",
    val priorityScore: Int = 50,
    val priorityBand: String = "STANDARD",
    val isEmergency: Boolean = false,
    val departments: List<DepartmentRefModel> = emptyList(),
    val createdAt: String? = null
)

@Serializable
data class DepartmentRefModel(
    val code: String,
    val name: String
)

@Serializable
data class GeoRoutingModel(
    val ward: String? = null,
    val zone: String? = null,
    val division: String? = null
)

@Serializable
data class ActiveReportModel(
    @SerialName("publicReference") val publicReference: String? = null,
    @SerialName("public_reference") val publicReferenceSnake: String? = null,
    val title: String,
    val status: String = "CONFIRMED",
    val severity: String = "MEDIUM",
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("created_at") val createdAtSnake: String? = null
) {
    val effectiveReference: String
        get() = publicReference ?: publicReferenceSnake ?: "NAG-2026-000000"

    val effectiveCreatedAt: String
        get() = createdAt ?: createdAtSnake ?: ""
}

@Serializable
data class ActiveReportsResponse(
    val success: Boolean = true,
    val incidents: List<ActiveReportModel> = emptyList()
)

@Serializable
data class RegisterGuestResponse(
    val success: Boolean = true,
    val guestId: String? = null
)

data class LocalDraftState(
    val text: String = "",
    val photoUris: List<String> = emptyList(),
    val locationText: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val selectedCategory: String? = null,
    val source: String = "text" // "text", "voice", "category"
)
