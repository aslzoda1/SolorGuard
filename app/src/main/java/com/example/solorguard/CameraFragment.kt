package com.example.solorguard

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.VideoView
import android.widget.TextView
import android.graphics.Color
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class CameraFragment : Fragment() {

    private var isThermalMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoView = view.findViewById<VideoView>(R.id.videoStreamView)
        val btnSwitch = view.findViewById<Button>(R.id.btnSwitchMode)
        val statusText = view.findViewById<TextView>(R.id.connectionStatus)

        // 1. Video oqimini sozlash (soxta video)
        // Eslatma: res/raw ichida  bo'lishi kerak
        val videoPath = "android.resource://" + requireActivity().packageName + "/" + R.raw.pi
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true // Video tugasa yana boshidan boshlanadi
            statusText.visibility = View.GONE // Video yuklangach matn yo'qoladi
            videoView.start()
        }

        // 2. Thermal Mode tugmasi mantiqi
        btnSwitch.setOnClickListener {
            isThermalMode = !isThermalMode

            if (isThermalMode) {
                btnSwitch.text = "Switch Normal Mode"
                // Bu yerda videoga qizil/ko'k filtr qo'yish yoki boshqa video yuklash mumkin
                videoView.alpha = 0.8f // Effekt uchun vizual o'zgarish
                Snackbar.make(view, "Thermal Analysis Activated", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.parseColor("#007AFF"))
                    .show()
            } else {
                btnSwitch.text = "Switch Thermal Mode"
                videoView.alpha = 1.0f
                Snackbar.make(view, "Standard Monitoring Mode", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}