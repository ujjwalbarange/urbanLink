package com.nagpur.connect.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nagpur.connect.ui.components.CitizenDrawerContent
import com.nagpur.connect.ui.components.CitizenTopAppBar
import com.nagpur.connect.ui.screens.emergency.EmergencyHubScreen
import com.nagpur.connect.ui.screens.home.CitizenHomeScreen
import com.nagpur.connect.ui.screens.report.AnalysisResultScreen
import com.nagpur.connect.ui.screens.report.AnalyzingScreen
import com.nagpur.connect.ui.screens.report.ComposingScreen
import com.nagpur.connect.ui.screens.report.DashboardUiView
import com.nagpur.connect.ui.screens.report.DeptMismatchScreen
import com.nagpur.connect.ui.screens.report.DeptQuestionsScreen
import com.nagpur.connect.ui.screens.report.DraftPreviewScreen
import com.nagpur.connect.ui.screens.report.FinalReviewScreen
import com.nagpur.connect.ui.screens.report.ReportSuccessScreen
import com.nagpur.connect.ui.screens.report.ReportViewModel
import com.nagpur.connect.ui.screens.track.MyReportsScreen
import com.nagpur.connect.ui.screens.track.TrackIncidentScreen
import com.nagpur.connect.ui.theme.CivicTheme
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel = viewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val currentView by reportViewModel.currentView.collectAsState()
    val draft by reportViewModel.draft.collectAsState()
    val activeReports by reportViewModel.activeReports.collectAsState()
    val errorMessage by reportViewModel.errorMessage.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CitizenDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTrackReport = { reference ->
                    navController.navigate(Screen.TrackIncident.createRoute(reference))
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                // Top app bar shown on Home when in default view or on sub-screens
                CitizenTopAppBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onEmergencyClick = { navController.navigate(Screen.Emergency.route) },
                    onTitleClick = {
                        reportViewModel.goHome()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            },
            containerColor = CivicTheme.colors.canvas
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // ── Home & Reporting State Machine ───────────────
                    composable(Screen.Home.route) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Error banner overlay matching web
                            errorMessage?.let { err ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CivicTheme.colors.criticalBg)
                                        .border(1.dp, CivicTheme.colors.criticalBorder, RoundedCornerShape(12.dp))
                                        .padding(14.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Text(text = "⚠️", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = err,
                                                fontSize = 13.sp,
                                                color = CivicTheme.colors.critical
                                            )
                                            Text(
                                                text = "Dismiss",
                                                fontSize = 11.sp,
                                                color = CivicTheme.colors.textTertiary,
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .clickable { reportViewModel.dismissError() }
                                            )
                                        }
                                    }
                                }
                            }

                            // Dynamic View according to the citizen reporting state machine
                            when (val v = currentView) {
                                is DashboardUiView.Home -> {
                                    CitizenHomeScreen(
                                        activeReports = activeReports,
                                        onSelectDepartment = { code ->
                                            reportViewModel.startComposing(source = "category", categorySlug = code)
                                        },
                                        onStartTextCompose = {
                                            reportViewModel.startComposing(source = "text")
                                        },
                                        onStartVoiceCompose = {
                                            reportViewModel.startComposing(source = "voice")
                                        },
                                        onViewReport = { ref ->
                                            navController.navigate(Screen.TrackIncident.createRoute(ref))
                                        },
                                        onViewAllReports = {
                                            navController.navigate(Screen.MyReports.route)
                                        }
                                    )
                                }

                                is DashboardUiView.Composing -> {
                                    ComposingScreen(
                                        draft = draft,
                                        onUpdateText = { reportViewModel.updateDraftText(it) },
                                        onAddPhoto = { reportViewModel.addPhoto(it) },
                                        onRemovePhoto = { reportViewModel.removePhoto(it) },
                                        onSetLocation = { loc, lat, lng -> reportViewModel.setLocation(loc, lat, lng) },
                                        onClearCategory = { reportViewModel.setCategory(null) },
                                        onCancel = { reportViewModel.goHome() },
                                        onReviewReport = { reportViewModel.goToPreview() }
                                    )
                                }

                                is DashboardUiView.Preview -> {
                                    DraftPreviewScreen(
                                        draft = draft,
                                        onEditText = { reportViewModel.goBackToComposing() },
                                        onRecordAgain = {
                                            reportViewModel.updateDraftText("")
                                            reportViewModel.startComposing(source = "voice")
                                        },
                                        onAddPhoto = { reportViewModel.addPhoto(it) },
                                        onRemovePhoto = { reportViewModel.removePhoto(it) },
                                        onEditLocation = { reportViewModel.goBackToComposing() },
                                        onAnalyze = { reportViewModel.runAnalysis() },
                                        onCancel = { reportViewModel.goHome() }
                                    )
                                }

                                is DashboardUiView.Analyzing -> {
                                    AnalyzingScreen(
                                        title = "Analyzing Your Report",
                                        subtitle = "AI is understanding your problem...",
                                        extraNote = "Stage 1 + 2 — takes ~3–5 seconds"
                                    )
                                }

                                is DashboardUiView.Mismatch -> {
                                    DeptMismatchScreen(
                                        analysis = v.analysis,
                                        selectedDepartmentSlug = v.selectedCategory,
                                        onAcceptSuggested = { reportViewModel.handleMismatchAccept(it) },
                                        onOverride = { reportViewModel.handleMismatchOverride() },
                                        onBack = { reportViewModel.goToPreview() }
                                    )
                                }

                                is DashboardUiView.Analysis -> {
                                    AnalysisResultScreen(
                                        analysis = v.result,
                                        onContinue = { reportViewModel.handleAnalysisContinue() },
                                        onBack = { reportViewModel.goToPreview() }
                                    )
                                }

                                is DashboardUiView.DeptQuestions -> {
                                    DeptQuestionsScreen(
                                        analysis = v.result,
                                        onSubmitAnswers = { reportViewModel.handleAnswersSubmitted(it) },
                                        onBack = { reportViewModel.handleMismatchOverride() }
                                    )
                                }

                                is DashboardUiView.Finalizing -> {
                                    AnalyzingScreen(
                                        title = "Generating Final Report",
                                        subtitle = "AI is summarizing your answers...",
                                        extraNote = "Stage 3 — takes ~2–3 seconds"
                                    )
                                }

                                is DashboardUiView.FinalReview -> {
                                    FinalReviewScreen(
                                        analysis = v.analysis,
                                        finalReport = v.finalReport,
                                        onSubmitReport = { reportViewModel.handleProceed() },
                                        onBack = { reportViewModel.handleAnalysisContinue() }
                                    )
                                }

                                is DashboardUiView.Submitting -> {
                                    AnalyzingScreen(
                                        title = "Creating Your Report",
                                        subtitle = "Saving to database...",
                                        extraNote = "Notifying relevant departments"
                                    )
                                }

                                is DashboardUiView.Success -> {
                                    ReportSuccessScreen(
                                        incident = v.incident,
                                        onViewReport = { ref ->
                                            reportViewModel.goHome()
                                            navController.navigate(Screen.TrackIncident.createRoute(ref))
                                        },
                                        onReportAnother = {
                                            reportViewModel.startComposing(source = "text")
                                        },
                                        onGoHome = {
                                            reportViewModel.goHome()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // ── My Reports Route ─────────────────────────────
                    composable(Screen.MyReports.route) {
                        MyReportsScreen(
                            reports = activeReports,
                            onSelectReport = { ref ->
                                navController.navigate(Screen.TrackIncident.createRoute(ref))
                            },
                            onReportNew = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                                reportViewModel.startComposing(source = "text")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ── Track Incident Route ─────────────────────────
                    composable(
                        route = Screen.TrackIncident.route,
                        arguments = listOf(
                            navArgument("reference") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val reference = backStackEntry.arguments?.getString("reference")
                        TrackIncidentScreen(
                            initialReference = reference,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ── Emergency Help Route ─────────────────────────
                    composable(Screen.Emergency.route) {
                        EmergencyHubScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
