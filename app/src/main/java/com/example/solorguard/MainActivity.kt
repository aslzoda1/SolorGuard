package com.example.solorguard

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarTitle: TextView
    private val sharedPrefs by lazy { getSharedPreferences("SolarGuard_Prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Temani qo'llash
        applyUserTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. UI elementlarni init qilish
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbarTitle = findViewById(R.id.text_title)
        val navigationView = findViewById<NavigationView>(R.id.nav_drawer)

        // Toolbar navigatsiyasi (Gamburger tugmasi)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 3. Drawer Menu Listener (About va boshqa menyular uchun)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            navigateToFragment(menuItem.itemId)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // 4. Bottom Bar va boshqa elementlarni sozlash
        setupBottomNavigation()

        // 5. Oxirgi fragmentni ochish
        val lastFragmentId = sharedPrefs.getInt("LAST_FRAGMENT_ID", R.id.homeFragment)
        navigateToFragment(lastFragmentId)

        // 6. ORQAGA QAYTISH TIZIMI
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    val lastId = sharedPrefs.getInt("LAST_FRAGMENT_ID", R.id.homeFragment)
                    if (lastId != R.id.homeFragment) {
                        navigateToFragment(R.id.homeFragment)
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                        isEnabled = true
                    }
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        findViewById<ImageView>(R.id.navHome).setOnClickListener { navigateToFragment(R.id.homeFragment) }
        findViewById<ImageView>(R.id.navCamera).setOnClickListener { navigateToFragment(R.id.cameraFragment) }
        findViewById<ImageView>(R.id.navStats).setOnClickListener { navigateToFragment(R.id.chartFragment) }
        findViewById<ImageView>(R.id.navSettings).setOnClickListener { navigateToFragment(R.id.settingsFragment) }

        findViewById<ImageView>(R.id.userss).setOnClickListener {
            navigateToFragment(R.id.profileFragment)
        }
    }

    fun navigateToFragment(id: Int) {
        val fragment = when (id) {
            R.id.homeFragment -> HomeFragment()
            R.id.cameraFragment -> CameraFragment()
            R.id.chartFragment -> StatsFragment()
            R.id.settingsFragment -> SettingsFragment()
            R.id.aboutFragment -> AboutFragment()
            R.id.profileFragment -> ProfileFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.nav_host, fragment)
            .commit()

        toolbarTitle.text = when (id) {
            R.id.profileFragment -> "Profile"
            R.id.homeFragment -> "Dashboard"
            R.id.cameraFragment -> "Live Monitor"
            R.id.chartFragment -> "Analytics"
            R.id.settingsFragment -> "Settings"
            R.id.aboutFragment -> "About Us"
            else -> "SolarGuard"
        }

        // Menyuda bosilgan elementni belgilab qo'yish (highlight)
        val navigationView = findViewById<NavigationView>(R.id.nav_drawer)
        navigationView.setCheckedItem(id)

        sharedPrefs.edit().putInt("LAST_FRAGMENT_ID", id).apply()
    }

    private fun applyUserTheme() {
        val isDark = sharedPrefs.getBoolean("DARK_MODE", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}