package com.example.myapplication.tools.measure

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class NoiseActivity : AppCompatActivity() {

    private lateinit var tvDecibel: TextView
    private lateinit var tvLevel: TextView
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noise)

        tvDecibel = findViewById(R.id.tv_decibel)
        tvLevel = findViewById(R.id.tv_level)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 100)
        } else {
            startMonitoring()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startMonitoring()
        }
    }

    private fun startMonitoring() {
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile("/dev/null")
                prepare()
                start()
                isRecording = true
            }
            Thread {
                while (isRecording) {
                    try {
                        val amplitude = mediaRecorder?.maxAmplitude ?: 1
                        val db = 20 * Math.log10(amplitude.toDouble()).toFloat()
                        runOnUiThread {
                            tvDecibel.text = String.format("%.1f dB", db)
                            tvLevel.text = when {
                                db < 30 -> "非常安静"
                                db < 50 -> "安静"
                                db < 70 -> "正常交谈"
                                db < 85 -> "吵闹"
                                db < 100 -> "非常吵闹"
                                else -> "伤害性噪音"
                            }
                        }
                        Thread.sleep(500)
                    } catch (e: Exception) { break }
                }
            }.start()
        } catch (e: Exception) {
            runOnUiThread { tvDecibel.text = "启动失败" }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRecording = false
        try {
            mediaRecorder?.apply { stop(); release() }
        } catch (e: Exception) {}
        mediaRecorder = null
    }
}
