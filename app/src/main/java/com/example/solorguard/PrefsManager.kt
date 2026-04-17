package com.example.solorguard

import android.content.Context

class PrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("SolarGuardPrefs", Context.MODE_PRIVATE)

    // Dark Mode holatini saqlash
    var isDarkMode: Boolean
        get() = prefs.getBoolean("isDarkMode", false)
        set(value) = prefs.edit().putBoolean("isDarkMode", value).apply()

    // Oxirgi fragment ID sini saqlash
    var lastFragmentId: Int
        get() = prefs.getInt("lastFragmentId", R.id.nav_home)
        set(value) = prefs.edit().putInt("lastFragmentId", value).apply()
}