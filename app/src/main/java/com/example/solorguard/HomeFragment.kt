package com.example.solorguard

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var tvEfficiency: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvActivePanels: TextView
    private lateinit var videoView: VideoView

    // Videolar ro'yxati (res/raw ichidagi fayl nomlari)
    private val videoList = listOf(R.raw.home, R.raw.bu)
    private var currentVideoIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvEfficiency = view.findViewById(R.id.tv_efficiency)
        tvTemp = view.findViewById(R.id.tv_temp)
        tvActivePanels = view.findViewById(R.id.tv_active_panels)
        videoView = view.findViewById(R.id.panel_video_view)

        // 1. Videoni boshlash
        playCurrentVideo()

        // 2. Video tugaganini eshitish va keyingisiga o'tish
        setupVideoSwitching()

        // 3. Real-vaqtli yangilanishlar
        startLiveUpdates()
    }

    private fun playCurrentVideo() {
        val videoRes = videoList[currentVideoIndex]
        val videoPath = "android.resource://${requireContext().packageName}/$videoRes"
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.setVolume(0f, 0f) // Ovozni o'chirish
            videoView.start()
        }
    }

    private fun setupVideoSwitching() {
        videoView.setOnCompletionListener {
            // Indeksni oshiramiz, agar oxirgi video bo'lsa, yana 0 ga qaytadi
            currentVideoIndex = (currentVideoIndex + 1) % videoList.size

            // Keyingi videoni qo'yamiz
            playCurrentVideo()
        }

        videoView.setOnErrorListener { _, _, _ ->
            true // Xatolik bo'lsa ilova yopilib ketmaydi
        }
    }

    private fun startLiveUpdates() {
        val handler = Handler(Looper.getMainLooper())
        val random = Random()

        handler.post(object : Runnable {
            override fun run() {
                val currentTemp = 34.0 + random.nextDouble() * 0.4
                tvTemp.text = String.format("%.1f°C", currentTemp)

                val efficiency = 98.0 + random.nextDouble() * 0.9
                tvEfficiency.text = String.format("%.1f%%", efficiency)

                handler.postDelayed(this, 3000)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        videoView.start()
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }
}