package com.example.solorguard

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML fragment_stats faylingizni ulaymiz
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<LineChart>(R.id.mainChart)
        if (chart != null) {
            setupPredictionChart(chart)
        }
        val videoView = view.findViewById<VideoView>(R.id.predictionVideoView)
        val videoPath = "android.resource://" + requireActivity().packageName + "/" + R.raw.vi
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true // Video to'xtovsiz aylanadi
            // Videoni CardView ichiga sig'dirish (Aspect Ratio)
            val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
            val screenRatio = videoView.width / videoView.height.toFloat()
            val scale = videoRatio / screenRatio
            if (scale >= 1f) videoView.scaleX = scale else videoView.scaleY = 1f / scale

            videoView.start()
        }
    }

    private fun setupPredictionChart(chart: LineChart) {
        val entries = ArrayList<Entry>()
        // Simulatsiya: Kelajakdagi soatlar va harorat
        entries.add(Entry(0f, 32f))
        entries.add(Entry(1f, 35f))
        entries.add(Entry(2f, 33f))
        entries.add(Entry(3f, 38f))
        entries.add(Entry(4f, 42f))
        entries.add(Entry(5f, 40f))

        val dataSet = LineDataSet(entries, "Predicted Temp (°C)")

        // iOS Style Design
        dataSet.color = Color.parseColor("#007AFF") // iOS Blue
        dataSet.setCircleColor(Color.parseColor("#007AFF"))
        dataSet.lineWidth = 4f
        dataSet.circleRadius = 5f
        dataSet.setDrawCircleHole(true)
        dataSet.circleHoleColor = Color.WHITE
        dataSet.valueTextSize = 10f
        dataSet.valueTextColor = Color.parseColor("#8E8E93") // iOS Gray

        // Grafik ostini bo'yash (Gradient effekti uchun)
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 50
        dataSet.fillColor = Color.parseColor("#007AFF")
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Egri chiziq

        val lineData = LineData(dataSet)
        chart.data = lineData

        // Chart Sozlamalari (Toza va Minimalist bo'lishi uchun)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        // O'qlarni (Axes) sozlash
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false) // Vertikal chiziqlarni o'chirish
        xAxis.textColor = Color.parseColor("#8E8E93")
        xAxis.granularity = 1f

        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = Color.parseColor("#E5E5EA") // Juda och chiziq
        leftAxis.textColor = Color.parseColor("#8E8E93")
        leftAxis.setDrawAxisLine(false)

        chart.axisRight.isEnabled = false // O'ng tomondagi o'qni o'chirish

        chart.animateY(1500) // Kirish animatsiyasi
        chart.invalidate() // Yangilash
    }
}