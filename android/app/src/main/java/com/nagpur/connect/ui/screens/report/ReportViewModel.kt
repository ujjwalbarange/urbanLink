package com.nagpur.connect.ui.screens.report

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nagpur.connect.data.model.ActiveReportModel
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.data.model.AttachmentPayload
import com.nagpur.connect.data.model.CreateIncidentRequest
import com.nagpur.connect.data.model.CreatedIncidentModel
import com.nagpur.connect.data.model.GroqFinalReportModel
import com.nagpur.connect.data.repository.IncidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream

sealed interface DashboardUiView {
    object Home : DashboardUiView
    object Composing : DashboardUiView
    object Preview : DashboardUiView
    object Analyzing : DashboardUiView
    data class Mismatch(
        val analysis: AnalysisResultModel,
        val selectedCategory: String
    ) : DashboardUiView
    data class Analysis(val result: AnalysisResultModel) : DashboardUiView
    data class DeptQuestions(val result: AnalysisResultModel) : DashboardUiView
    object Finalizing : DashboardUiView
    data class FinalReview(
        val analysis: AnalysisResultModel,
        val finalReport: GroqFinalReportModel?
    ) : DashboardUiView
    object Submitting : DashboardUiView
    data class Success(val incident: CreatedIncidentModel) : DashboardUiView
}

data class IncidentDraftState(
    val text: String = "",
    val photoUris: List<Uri> = emptyList(),
    val locationText: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val selectedCategory: String? = null,
    val source: String = "text"
) {
    val isReadyForAnalysis: Boolean
        get() = text.trim().length >= 10
}

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = IncidentRepository(application)

    private val _currentView = MutableStateFlow<DashboardUiView>(DashboardUiView.Home)
    val currentView: StateFlow<DashboardUiView> = _currentView.asStateFlow()

    private val _draft = MutableStateFlow(IncidentDraftState())
    val draft: StateFlow<IncidentDraftState> = _draft.asStateFlow()

    private val _activeReports = MutableStateFlow<List<ActiveReportModel>>(emptyList())
    val activeReports: StateFlow<List<ActiveReportModel>> = _activeReports.asStateFlow()

    private val _deptAnswers = MutableStateFlow<Map<String, String>>(emptyMap())
    val deptAnswers: StateFlow<Map<String, String>> = _deptAnswers.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var currentAnalysisResult: AnalysisResultModel? = null
    private var currentFinalReport: GroqFinalReportModel? = null

    init {
        loadActiveReports()
    }

    fun loadActiveReports() {
        viewModelScope.launch {
            repository.getMyIncidents().onSuccess {
                _activeReports.value = it
            }
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    fun startComposing(source: String = "text", categorySlug: String? = null) {
        _errorMessage.value = null
        _deptAnswers.value = emptyMap()
        currentAnalysisResult = null
        currentFinalReport = null

        _draft.value = IncidentDraftState(
            source = source,
            selectedCategory = categorySlug
        )
        _currentView.value = DashboardUiView.Composing
    }

    fun updateDraftText(text: String) {
        _draft.value = _draft.value.copy(text = text)
    }

    fun addPhoto(uri: Uri) {
        if (_draft.value.photoUris.size < 3) {
            _draft.value = _draft.value.copy(
                photoUris = _draft.value.photoUris + uri
            )
        }
    }

    fun removePhoto(index: Int) {
        val updated = _draft.value.photoUris.toMutableList()
        if (index in updated.indices) {
            updated.removeAt(index)
            _draft.value = _draft.value.copy(photoUris = updated)
        }
    }

    fun setLocation(locationText: String, lat: Double? = null, lng: Double? = null) {
        _draft.value = _draft.value.copy(
            locationText = locationText,
            latitude = lat,
            longitude = lng
        )
    }

    fun setCategory(categorySlug: String?) {
        _draft.value = _draft.value.copy(selectedCategory = categorySlug)
    }

    fun goToPreview() {
        _currentView.value = DashboardUiView.Preview
    }

    fun goBackToComposing() {
        _currentView.value = DashboardUiView.Composing
    }

    fun goHome() {
        _currentView.value = DashboardUiView.Home
        _draft.value = IncidentDraftState()
        _deptAnswers.value = emptyMap()
        currentAnalysisResult = null
        currentFinalReport = null
        _errorMessage.value = null
        loadActiveReports()
    }

    // ── AI Analysis Trigger ─────────────────────────────
    fun runAnalysis(overrideDept: String? = null) {
        val draftVal = _draft.value
        if (!draftVal.isReadyForAnalysis) {
            _errorMessage.value = "Please provide at least 10 characters to analyze."
            return
        }

        _errorMessage.value = null
        _currentView.value = DashboardUiView.Analyzing

        val deptToSend = overrideDept ?: draftVal.selectedCategory

        viewModelScope.launch {
            repository.analyzeIncident(
                text = draftVal.text,
                locationContext = draftVal.locationText.ifBlank { null },
                selectedDepartment = deptToSend
            ).onSuccess { analysis ->
                currentAnalysisResult = analysis
                if (analysis.mismatch && !deptToSend.isNullOrBlank()) {
                    _currentView.value = DashboardUiView.Mismatch(analysis, deptToSend)
                } else {
                    _currentView.value = DashboardUiView.Analysis(analysis)
                }
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Analysis failed. Please try again."
                _currentView.value = DashboardUiView.Preview
            }
        }
    }

    fun handleMismatchAccept(correctDept: String) {
        setCategory(correctDept)
        runAnalysis(overrideDept = correctDept)
    }

    fun handleMismatchOverride() {
        currentAnalysisResult?.let {
            _currentView.value = DashboardUiView.Analysis(it)
        }
    }

    fun handleAnalysisContinue() {
        val analysis = currentAnalysisResult ?: return
        if (analysis.deptQuestions.isNotEmpty()) {
            _currentView.value = DashboardUiView.DeptQuestions(analysis)
        } else {
            // No questions — go straight to finalize
            _currentView.value = DashboardUiView.Finalizing
            viewModelScope.launch {
                repository.finalizeIncident(
                    originalText = _draft.value.text,
                    analysis = analysis,
                    answers = emptyMap(),
                    locationText = _draft.value.locationText.ifBlank { null }
                ).onSuccess { finalRep ->
                    currentFinalReport = finalRep
                    _currentView.value = DashboardUiView.FinalReview(analysis, finalRep)
                }
            }
        }
    }

    fun handleAnswersSubmitted(answers: Map<String, String>) {
        val analysis = currentAnalysisResult ?: return
        _deptAnswers.value = answers
        _currentView.value = DashboardUiView.Finalizing

        viewModelScope.launch {
            repository.finalizeIncident(
                originalText = _draft.value.text,
                analysis = analysis,
                answers = answers,
                locationText = _draft.value.locationText.ifBlank { null }
            ).onSuccess { finalRep ->
                currentFinalReport = finalRep
                _currentView.value = DashboardUiView.FinalReview(analysis, finalRep)
            }
        }
    }

    // ── Final Incident Submission ───────────────────────
    fun handleProceed() {
        val analysis = currentAnalysisResult ?: return
        _errorMessage.value = null
        _currentView.value = DashboardUiView.Submitting

        viewModelScope.launch {
            val attachments = convertUrisToAttachments(_draft.value.photoUris)

            val request = CreateIncidentRequest(
                originalText = _draft.value.text,
                analysis = analysis,
                locationText = _draft.value.locationText.ifBlank { analysis.location.text },
                latitude = _draft.value.latitude ?: analysis.location.lat,
                longitude = _draft.value.longitude ?: analysis.location.lng,
                departmentAnswers = _deptAnswers.value,
                selectedDepartment = _draft.value.selectedCategory,
                finalReport = currentFinalReport,
                attachments = attachments
            )

            repository.createIncident(request).onSuccess { response ->
                if (response.incident != null) {
                    _currentView.value = DashboardUiView.Success(response.incident)
                    loadActiveReports()
                } else {
                    _errorMessage.value = "Failed to create incident."
                    _currentView.value = DashboardUiView.FinalReview(analysis, currentFinalReport)
                }
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to submit report."
                _currentView.value = DashboardUiView.FinalReview(analysis, currentFinalReport)
            }
        }
    }

    private fun convertUrisToAttachments(uris: List<Uri>): List<AttachmentPayload> {
        val context = getApplication<Application>()
        return uris.mapIndexedNotNull { index, uri ->
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
                val bytes = outputStream.toByteArray()
                val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                AttachmentPayload(
                    fileName = "evidence_${index + 1}.jpg",
                    mimeType = "image/jpeg",
                    fileSize = bytes.size.toLong(),
                    storageUrl = "data:image/jpeg;base64,$base64"
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
