package com.nagpur.connect.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nagpur.connect.data.model.ActiveReportModel
import com.nagpur.connect.data.model.AnalysisLocationModel
import com.nagpur.connect.data.model.AnalysisResultModel
import com.nagpur.connect.data.model.CreatedIncidentModel
import com.nagpur.connect.data.model.DeptQuestionModel
import com.nagpur.connect.data.model.GroqFinalReportModel
import com.nagpur.connect.data.model.NotifiedDeptModel
import com.nagpur.connect.data.model.SeverityInfoModel
import com.nagpur.connect.ui.screens.emergency.EmergencyHubScreen
import com.nagpur.connect.ui.screens.home.CitizenHomeScreen
import com.nagpur.connect.ui.screens.report.AnalysisResultScreen
import com.nagpur.connect.ui.screens.report.ComposingScreen
import com.nagpur.connect.ui.screens.report.DeptQuestionsScreen
import com.nagpur.connect.ui.screens.report.DraftPreviewScreen
import com.nagpur.connect.ui.screens.report.FinalReviewScreen
import com.nagpur.connect.ui.screens.report.IncidentDraftState
import com.nagpur.connect.ui.screens.report.ReportSuccessScreen
import com.nagpur.connect.ui.theme.NagpurConnectTheme

@Preview(name = "1. Home Dashboard", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewCitizenHome() {
    NagpurConnectTheme {
        CitizenHomeScreen(
            activeReports = listOf(
                ActiveReportModel(
                    publicReference = "NAG-2026-000108",
                    title = "Severe Pothole Hazard near Sitabuldi Metro Station",
                    status = "IN_PROGRESS",
                    severity = "HIGH",
                    createdAt = "2026-10-02"
                )
            ),
            onSelectDepartment = {},
            onStartTextCompose = {},
            onStartVoiceCompose = {},
            onViewReport = {},
            onViewAllReports = {}
        )
    }
}

@Preview(name = "2. Composing Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewComposing() {
    NagpurConnectTheme {
        ComposingScreen(
            draft = IncidentDraftState(
                text = "Large water pipe leakage at Dharampeth market causing water accumulation on main road.",
                locationText = "Dharampeth, Nagpur",
                selectedCategory = "water_supply"
            ),
            onUpdateText = {},
            onAddPhoto = {},
            onRemovePhoto = {},
            onSetLocation = { _, _, _ -> },
            onClearCategory = {},
            onCancel = {},
            onReviewReport = {}
        )
    }
}

@Preview(name = "3. Draft Preview", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewDraftPreview() {
    NagpurConnectTheme {
        DraftPreviewScreen(
            draft = IncidentDraftState(
                text = "Water pipeline ruptured near Sitabuldi square causing severe flooding and traffic delay.",
                locationText = "Sitabuldi, Nagpur (GPS)",
                source = "text"
            ),
            onEditText = {},
            onRecordAgain = {},
            onAddPhoto = {},
            onRemovePhoto = {},
            onEditLocation = {},
            onAnalyze = {},
            onCancel = {}
        )
    }
}

@Preview(name = "4. AI Analysis Result", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewAnalysisResult() {
    NagpurConnectTheme {
        AnalysisResultScreen(
            analysis = AnalysisResultModel(
                mainCategory = "roads_traffic",
                mainCategoryName = "Roads & Traffic",
                incidentType = "Pothole Hazard",
                title = "Hazardous Pothole on Sitabuldi Metro Road",
                summary = "Critical pothole cluster causing hazardous skidding for two-wheelers and heavy evening transit congestion.",
                severity = SeverityInfoModel(level = "high", score = 78, reason = "Presents active collision risk on primary arterial road."),
                departments = listOf(
                    NotifiedDeptModel(code = "pwd", name = "Public Works Department", isPrimary = true, reason = "Road surface asphalt repair"),
                    NotifiedDeptModel(code = "traffic_police", name = "Traffic Police", isPrimary = false, reason = "Traffic diversion and warning cones")
                ),
                location = AnalysisLocationModel(text = "Sitabuldi, Ward 12, Nagpur"),
                deptQuestions = listOf(
                    DeptQuestionModel(id = "q1", question = "Is the pothole deeper than 6 inches?", type = "chip", options = listOf("Yes, very deep", "Moderate depth", "Minor surface wear"))
                )
            ),
            onContinue = {},
            onBack = {}
        )
    }
}

@Preview(name = "5. Dynamic Questions", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewDeptQuestions() {
    NagpurConnectTheme {
        DeptQuestionsScreen(
            analysis = AnalysisResultModel(
                mainCategory = "roads_traffic",
                mainCategoryName = "Roads & Traffic",
                summary = "Pothole causing accidents near Sitabuldi station.",
                severity = SeverityInfoModel(level = "high", score = 78),
                deptQuestions = listOf(
                    DeptQuestionModel(
                        id = "q1",
                        question = "Is the pothole deeper than 6 inches or affecting major transit?",
                        type = "chip",
                        options = listOf("Yes, very deep", "Moderate depth", "Minor surface wear"),
                        required = true
                    ),
                    DeptQuestionModel(
                        id = "q2",
                        question = "Is traffic actively blocked or diverted?",
                        type = "yesno",
                        options = listOf("Yes", "No", "Unsure"),
                        required = false
                    )
                )
            ),
            onSubmitAnswers = {},
            onBack = {}
        )
    }
}

@Preview(name = "6. Final Review", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewFinalReview() {
    NagpurConnectTheme {
        FinalReviewScreen(
            analysis = AnalysisResultModel(
                mainCategory = "roads_traffic",
                mainCategoryName = "Roads & Traffic",
                summary = "Dangerous pothole hazard at Sitabuldi metro station.",
                severity = SeverityInfoModel(level = "high", score = 78),
                departments = listOf(
                    NotifiedDeptModel(code = "pwd", name = "Public Works Department")
                )
            ),
            finalReport = GroqFinalReportModel(
                finalSummary = "Severe pothole on arterial metro route. Citizen confirmed depth exceeding 6 inches with active traffic disruption.",
                severity = "high",
                priorityScore = 82,
                affectedPeople = "2,000+ commuters",
                keyFindings = listOf(
                    "Depth exceeds 6 inches, posing skid risk to two-wheelers.",
                    "Arterial junction near metro entry point."
                ),
                recommendedActions = listOf(
                    "Deploy temporary barricading within 2 hours.",
                    "Cold asphalt patch repair dispatched to PWD Ward 12 crew."
                )
            ),
            onSubmitReport = {},
            onBack = {}
        )
    }
}

@Preview(name = "7. Success Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewReportSuccess() {
    NagpurConnectTheme {
        ReportSuccessScreen(
            incident = CreatedIncidentModel(
                publicReference = "NAG-2026-000108",
                title = "Dangerous Pothole Hazard near Sitabuldi Metro Station",
                severity = "HIGH",
                priorityScore = 82,
                isEmergency = false
            ),
            onViewReport = {},
            onReportAnother = {},
            onGoHome = {}
        )
    }
}

@Preview(name = "8. Emergency Hub", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewEmergencyHub() {
    NagpurConnectTheme {
        EmergencyHubScreen(
            onBack = {}
        )
    }
}

@Preview(name = "9. My Reports History", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreviewMyReports() {
    NagpurConnectTheme {
        com.nagpur.connect.ui.screens.track.MyReportsScreen(
            reports = listOf(
                ActiveReportModel(
                    publicReference = "NAG-2026-000108",
                    title = "Severe Pothole Hazard near Sitabuldi Metro Station",
                    status = "IN_PROGRESS",
                    severity = "HIGH",
                    department = "Public Works Department",
                    createdAt = "2026-10-02"
                ),
                ActiveReportModel(
                    publicReference = "NAG-2026-000095",
                    title = "Broken street light causing safety concerns on Wardha Road",
                    status = "RESOLVED",
                    severity = "MEDIUM",
                    department = "Electricity & Street Lighting",
                    createdAt = "2026-09-28"
                )
            ),
            onSelectReport = {},
            onReportNew = {},
            onBack = {}
        )
    }
}

