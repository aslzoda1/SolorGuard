package com.example.solorguard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // Panellarning harorati (Real-time ma'lumot simulatsiyasi)
    private val _currentTemp = MutableLiveData<Double>()
    val currentTemp: LiveData<Double> get() = _currentTemp

    private val _systemStatus = MutableLiveData<String>()
    val systemStatus: LiveData<String> get() = _systemStatus

    init {
        // Ilova ishga tushganda boshlang'ich qiymatlar
        _currentTemp.value = 34.5
        _systemStatus.value = "All systems operational"
    }

    // Harorat o'zgarganda xabar berish funksiyasi
    fun updateTemperature(newTemp: Double) {
        _currentTemp.value = newTemp
        if (newTemp > 70.0) {
            _systemStatus.value = "CRITICAL: Overheating detected!"
        } else {
            _systemStatus.value = "All systems operational"
        }
    }
}