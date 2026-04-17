package com.example.solorguard

data class SolarPanel(
    val id: String,
    val currentTemp: Double,
    val status: PanelStatus, // SAFE, WARNING, CRITICAL
    val lastUpdate: Long
)

enum class PanelStatus {
    SAFE, WARNING, CRITICAL
}