package com.example.myapplication.tools.measure

import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompassActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var compassView: CompassView
    private lateinit var tvDegree: TextView
    private lateinit var tvDirection: TextView
    private var currentAzimuth = 0f

    inner class CompassView(context: Context) : android.view.View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 40f; textAlign = Paint.Align.CENTER; color = Color.BLACK }

        override fun onDraw(canvas: Canvas) {
            val cx = width / 2f
            val cy = height / 2f
            val radius = minOf(cx, cy) * 0.7f

            paint.style = Paint.Style.STROKE; paint.strokeWidth = 4f
            canvas.drawCircle(cx, cy, radius, paint)

            for (i in 0 until 360 step 30) {
                val rad = Math.toRadians((i - 90).toDouble())
                val isMajor = i % 90 == 0
                val inner = if (isMajor) radius * 0.85f else radius * 0.92f
                val outer = radius
                canvas.drawLine(
                    cx + inner * Math.cos(rad).toFloat(), cy + inner * Math.sin(rad).toFloat(),
                    cx + outer * Math.cos(rad).toFloat(), cy + outer * Math.sin(rad).toFloat(),
                    paint
                )
            }

            val angle = Math.toRadians((-currentAzimuth).toDouble())
            paint.style = Paint.Style.FILL
            paint.color = Color.RED
            canvas.drawLine(cx, cy, cx + radius * 0.6f * Math.cos(angle).toFloat(), cy + radius * 0.6f * Math.sin(angle).toFloat(), paint)
            paint.color = Color.GRAY
            canvas.drawLine(cx, cy, cx - radius * 0.6f * Math.cos(angle).toFloat(), cy - radius * 0.6f * Math.sin(angle).toFloat(), paint)

            canvas.drawText("N", cx, cy - radius + 50f, textPaint)
            canvas.drawText("S", cx, cy + radius - 20f, textPaint)
            canvas.drawText("E", cx + radius - 30f, cy + 15f, textPaint)
            canvas.drawText("W", cx - radius + 30f, cy + 15f, textPaint)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        compassView = CompassView(this)
        tvDegree = TextView(this).apply { textSize = 20f; gravity = android.view.Gravity.CENTER }
        tvDirection = TextView(this).apply { textSize = 24f; gravity = android.view.Gravity.CENTER; typeface = Typeface.DEFAULT_BOLD }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(compassView, LinearLayout.LayoutParams(-1, 0, 1f))
            addView(tvDegree)
            addView(tvDirection)
        }
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        currentAzimuth = event.values[0]
        compassView.invalidate()
        tvDegree.text = String.format("%.0f\u00B0", currentAzimuth)
        tvDirection.text = when {
            currentAzimuth < 22.5f || currentAzimuth >= 337.5f -> "北"
            currentAzimuth < 67.5f -> "东北"
            currentAzimuth < 112.5f -> "东"
            currentAzimuth < 157.5f -> "东南"
            currentAzimuth < 202.5f -> "南"
            currentAzimuth < 247.5f -> "西南"
            currentAzimuth < 292.5f -> "西"
            else -> "西北"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
