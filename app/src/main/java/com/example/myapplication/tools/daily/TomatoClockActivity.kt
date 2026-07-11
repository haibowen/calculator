package com.example.myapplication.tools.daily

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class TomatoClockActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timer: CountDownTimer? = null
    private var isWorkTime = true
    private var timeLeftMs = WORK_DURATION_MS
    private var isRunning = false

    companion object {
        private const val WORK_DURATION_MS = 25 * 60 * 1000L
        private const val BREAK_DURATION_MS = 5 * 60 * 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomato_clock)

        tvTimer = findViewById(R.id.tv_timer)
        tvStatus = findViewById(R.id.tv_status)
        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)
        btnReset = findViewById(R.id.btn_reset)

        updateDisplay()

        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }

    private fun startTimer() {
        if (isRunning) return
        isRunning = true
        timer = object : CountDownTimer(timeLeftMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMs = millisUntilFinished
                updateDisplay()
            }

            override fun onFinish() {
                isWorkTime = !isWorkTime
                timeLeftMs = if (isWorkTime) WORK_DURATION_MS else BREAK_DURATION_MS
                isRunning = false
                tvStatus.text = if (isWorkTime) "工作中" else "休息中"
                updateDisplay()
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
        isWorkTime = true
        timeLeftMs = WORK_DURATION_MS
        tvStatus.text = "工作中"
        updateDisplay()
    }

    private fun updateDisplay() {
        val totalSec = timeLeftMs / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        tvTimer.text = String.format("%02d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
