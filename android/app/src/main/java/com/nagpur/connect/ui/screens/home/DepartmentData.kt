package com.nagpur.connect.ui.screens.home

data class CivicDepartment(
    val code: String,
    val name: String,
    val nameMarathi: String,
    val description: String,
    val icon: String,
    val priorityBand: String = "STANDARD"
)

val CIVIC_DEPARTMENTS: List<CivicDepartment> = listOf(
    CivicDepartment(
        code = "police",
        name = "Police Department",
        nameMarathi = "पोलीस विभाग",
        description = "Law enforcement and public safety",
        icon = "🛡️",
        priorityBand = "IMMEDIATE_EMERGENCY"
    ),
    CivicDepartment(
        code = "traffic_police",
        name = "Traffic Police",
        nameMarathi = "वाहतूक पोलीस",
        description = "Traffic regulation and accident response",
        icon = "🚦",
        priorityBand = "IMMEDIATE_EMERGENCY"
    ),
    CivicDepartment(
        code = "fire_brigade",
        name = "Fire Brigade",
        nameMarathi = "अग्निशामक दल",
        description = "Fire suppression and rescue operations",
        icon = "🔥",
        priorityBand = "IMMEDIATE_EMERGENCY"
    ),
    CivicDepartment(
        code = "health_dept",
        name = "Health Department",
        nameMarathi = "सार्वजनिक आरोग्य विभाग",
        description = "Public health and disease control",
        icon = "🏥",
        priorityBand = "URGENT"
    ),
    CivicDepartment(
        code = "ambulance",
        name = "Ambulance Services",
        nameMarathi = "रुग्णवाहिका सेवा",
        description = "Emergency medical transport",
        icon = "🚑",
        priorityBand = "IMMEDIATE_EMERGENCY"
    ),
    CivicDepartment(
        code = "water_supply",
        name = "Water Supply Department",
        nameMarathi = "पाणी पुरवठा विभाग",
        description = "Municipal water supply management",
        icon = "💧",
        priorityBand = "URGENT"
    ),
    CivicDepartment(
        code = "drainage",
        name = "Drainage Department",
        nameMarathi = "मलनिस्सारण विभाग",
        description = "Stormwater and sewage drainage",
        icon = "🌊",
        priorityBand = "URGENT"
    ),
    CivicDepartment(
        code = "road_maintenance",
        name = "Road Maintenance / PWD",
        nameMarathi = "रस्ते देखभाल / सार्वजनिक बांधकाम",
        description = "Road repair and public works",
        icon = "🛣️",
        priorityBand = "STANDARD"
    ),
    CivicDepartment(
        code = "traffic_management",
        name = "Traffic Management",
        nameMarathi = "वाहतूक व्यवस्थापन",
        description = "Signal maintenance and traffic flow",
        icon = "🚥",
        priorityBand = "STANDARD"
    ),
    CivicDepartment(
        code = "waste_management",
        name = "Waste Management",
        nameMarathi = "घनकचरा व्यवस्थापन",
        description = "Solid waste collection and disposal",
        icon = "🗑️",
        priorityBand = "STANDARD"
    ),
    CivicDepartment(
        code = "environment",
        name = "Environmental Department",
        nameMarathi = "पर्यावरण विभाग",
        description = "Environmental protection and green spaces",
        icon = "🌳",
        priorityBand = "STANDARD"
    ),
    CivicDepartment(
        code = "electricity",
        name = "Electricity Department",
        nameMarathi = "विद्युत विभाग",
        description = "Power supply and street lighting",
        icon = "⚡",
        priorityBand = "URGENT"
    )
)

fun getDepartmentByCode(code: String): CivicDepartment? {
    return CIVIC_DEPARTMENTS.find { it.code.equals(code, ignoreCase = true) }
}
