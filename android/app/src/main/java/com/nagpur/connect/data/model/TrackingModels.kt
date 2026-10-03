package com.nagpur.connect.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackingIncidentResponse(
    val success: Boolean = false,
    val incident: IncidentDetailModel? = null,
    val departments: List<DepartmentTrackingModel> = emptyList(),
    val timeline: List<TimelineEventModel> = emptyList(),
    val media: List<MediaItemModel> = emptyList(),
    val aiConversation: List<AIConversationTrackingModel> = emptyList(),
    val error: String? = null
)

@Serializable
data class IncidentDetailModel(
    val publicReference: String,
    val category: String? = null,
    val status: String = "CONFIRMED", // CONFIRMED, ROUTED, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED
    val severity: String? = "MEDIUM",
    val priorityScore: Int? = 50,
    val title: String? = null,
    val citizenSummary: String? = null,
    val locationText: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isEmergency: Boolean = false,
    val privacyLevel: String = "normal",
    val createdAt: String,
    val confirmedAt: String? = null,
    val resolvedAt: String? = null
)

@Serializable
data class DepartmentTrackingModel(
    val code: String,
    val name: String,
    val status: String = "PENDING"
)

@Serializable
data class TimelineEventModel(
    val status: String,
    val description: String? = null,
    val timestamp: String
)

@Serializable
data class MediaItemModel(
    val id: String = "",
    val fileName: String = "",
    val mimeType: String = "image/jpeg",
    val fileSize: Long = 0,
    val storageUrl: String? = null,
    val purpose: String = "evidence"
)

@Serializable
data class AIConversationTrackingModel(
    val questionId: String,
    val questionText: String,
    val questionType: String = "chip",
    val answerValue: String? = null,
    val isRequired: Boolean = false
)
