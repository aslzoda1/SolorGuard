package com.example.solorguard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.VideoView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<View>(R.id.logo_container)
        val title = findViewById<TextView>(R.id.text_title)
        val videoView = findViewById<VideoView>(R.id.splash_video)

        // 1. Dastlab Logo va Textni ko'rinmas qilib tayyorlaymiz
        logo.alpha = 0f
        logo.translationY = 30f // Ozgina pastdan yuqoriga chiqish effekti uchun
        title.alpha = 0f

        // 2. Logo va Title uchun animatsiya (Video bilan bir vaqtda boshlanadi)
        logo.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(1200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        title.animate()
            .alpha(1f)
            .setStartDelay(300) // Yozuv logodan biroz keyinroq chiqadi
            .setDuration(1000)
            .start()

        // 3. Videoni ishga tushirish
        playSplashVideo(videoView)
    }

    private fun playSplashVideo(videoView: VideoView) {
        val videoPath = "android.resource://" + packageName + "/" + R.raw.download
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = false
            videoView.start()
        }

        // Video tugashi bilan (sizda 5 soniya ekan) keyingi ekranga o'tadi
        videoView.setOnCompletionListener {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        // Silliq o'tish animatsiyasi
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        finish()
    }
}