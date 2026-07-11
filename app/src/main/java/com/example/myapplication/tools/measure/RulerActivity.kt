package com.example.myapplication.tools.measure

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class RulerActivity : AppCompatActivity() {

    private lateinit var rulerContainer: LinearLayout
    private lateinit var tvMeasurement: TextView
    private lateinit var toggleUnit: ToggleButton
    private lateinit var btnTouchRuler: Button

    private var isCm = true
    private var dpi = 160f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler)

        dpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        rulerContainer = findViewById(R.id.ruler_container)
        tvMeasurement = findViewById(R.id.tv_measurement)
        toggleUnit = findViewById(R.id.toggle_unit)
        btnTouchRuler = findViewById(R.id.btn_touch_ruler)

        drawRuler()

        toggleUnit.setOnCheckedChangeListener { _, isChecked ->
            isCm = !isChecked
            drawRuler()
        }

        btnTouchRuler.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                val cmPerPixel = 2.54f / dpi
                val cm = event.x * cmPerPixel
                tvMeasurement.text = if (isCm) String.format("%.1f cm", cm) else String.format("%.1f in", cm / 2.54f)
            }
            true
        }
    }

    private fun drawRuler() {
        rulerContainer.removeAllViews()
        val paint = Paint().apply { color = Color.BLACK; strokeWidth = 2f }
        val cmWidth = (dpi / 2.54f).toInt()

        for (i in 0 until 30) {
            val view = object : android.view.View(this) {
                override fun onDraw(canvas: Canvas) {
                    super.onDraw(canvas)
                    canvas.drawLine(0f, 0f, 0f, height.toFloat(), paint)
                    canvas.drawText("$i", 4f, height - 4f, Paint().apply { textSize = 24f })
                    for (j in 1..9) {
                        val x = cmWidth * j / 10f
                        val h = if (j == 5) height * 0.6f else height * 0.3f
                        canvas.drawLine(x, 0f, x, h, paint)
                    }
                }
            }
            view.layoutParams = LinearLayout.LayoutParams(cmWidth, 120)
            rulerContainer.addView(view)
        }
    }
}
