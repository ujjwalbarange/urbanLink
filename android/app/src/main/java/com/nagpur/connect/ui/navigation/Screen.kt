package com.nagpur.connect.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MyReports : Screen("my_reports")
    object Emergency : Screen("emergency")
    object TrackIncident : Screen("track?reference={reference}") {
        fun createRoute(ref: String? = null): String {
            return if (!ref.isNullOrBlank()) "track?reference=$ref" else "track"
        }
    }
}
