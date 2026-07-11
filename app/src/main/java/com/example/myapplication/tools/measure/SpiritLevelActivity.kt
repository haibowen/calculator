package com.example.myapplication.tools.measure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpiritLevelActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var levelView: LevelView
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView
    private var pitch = 0f
    private var roll = 0f

    inner class LevelView(context: Context) : android.view.View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(canvas: Canvas) {
            val cx = width / 2f
            val cy = height / 2f
            val radius = minOf(cx, cy) * 0.4f

            paint.style = Paint.Style.STROKE; paint.strokeWidth = 4f; paint.color = Color.DKGRAY
            canvas.drawCircle(cx, cy, radius, paint)

            paint.style = Paint.Style.FILL; paint.color = Color.argb(80, 0, 200, 0)
            val maxAngle = 15f
            val bx = cx + (roll / maxAngle) * radius
            val by = cy + (pitch / maxAngle) * radius
            val bubbleR = radius * 0.15f
            canvas.drawCircle(bx, by, bubbleR, paint)

            paint.style = Paint.Style.STROKE; paint.strokeWidth = 2f; paint.color = Color.GRAY
            canvas.drawLine(cx - radius, cy, cx + radius, cy, paint)
            canvas.drawLine(cx, cy - radius, cx, cy + radius, paint)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        levelView = LevelView(this)
        tvX = TextView(this).apply { gravity = android.view.Gravity.CENTER }
        tvY = TextView(this).apply { gravity = android.view.Gravity.CENTER }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(levelView, LinearLayout.LayoutParams(-1, 0, 1f))
            addView(tvX)
            addView(tvY)
        }
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        pitch = event.values[1] * 90f / 9.81f
        roll = event.values[0] * 90f / 9.81f
        levelView.invalidate()
        tvX.text = String.format("左右倾斜: %.1f°", roll)
        tvY.text = String.format("前后倾斜: %.1f°", pitch)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
