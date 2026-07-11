package com.example.myapplication.tools.daily

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class CountdownActivity : AppCompatActivity() {

    private lateinit var etHours: EditText
    private lateinit var etMinutes: EditText
    private lateinit var etSeconds: EditText
    private lateinit var tvCountdown: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timer: CountDownTimer? = null
    private var timeLeftMs = 0L
    private var totalMs = 0L
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        etHours = findViewById(R.id.et_hours)
        etMinutes = findViewById(R.id.et_minutes)
        etSeconds = findViewById(R.id.et_seconds)
        tvCountdown = findViewById(R.id.tv_countdown)
        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)
        btnReset = findViewById(R.id.btn_reset)

        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }

    private fun startTimer() {
        if (isRunning) return
        if (timeLeftMs <= 0) {
            val h = etHours.text.toString().toIntOrNull() ?: 0
            val m = etMinutes.text.toString().toIntOrNull() ?: 0
            val s = etSeconds.text.toString().toIntOrNull() ?: 0
            totalMs = (h * 3600L + m * 60L + s) * 1000L
            timeLeftMs = totalMs
        }
        if (timeLeftMs <= 0) return
        isRunning = true
        timer = object : CountDownTimer(timeLeftMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMs = millisUntilFinished
                updateDisplay()
            }

            override fun onFinish() {
                isRunning = false
                tvCountdown.text = "00:00:00"
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        timer?.cancel()
        isRunning = false
        timeLeftMs = 0
        tvCountdown.text = "00:00:00"
    }

    private fun updateDisplay() {
        val totalSec = timeLeftMs / 1000
        val h = totalSec / 3600
        val m = (totalSec % 3600) / 60
        val s = totalSec % 60
        tvCountdown.text = String.format("%02d:%02d:%02d", h, m, s)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
