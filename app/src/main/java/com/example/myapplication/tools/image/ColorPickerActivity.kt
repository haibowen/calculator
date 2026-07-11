package com.example.myapplication.tools.image

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import org.json.JSONArray

class ColorWheelView(context: Context, attrs: android.util.AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var onColorChanged: ((Int) -> Unit)? = null
    private val colors = intArrayOf(
        0xFFFF0000.toInt(), 0xFFFFFF00.toInt(), 0xFF00FF00.toInt(),
        0xFF00FFFF.toInt(), 0xFF0000FF.toInt(), 0xFFFF00FF.toInt(), 0xFFFF0000.toInt()
    )

    override fun onDraw(canvas: Canvas) {
        val cx = width / 2f; val cy = height / 2f; val r = minOf(cx, cy) * 0.8f
        val shader = SweepGradient(cx, cy, colors, null)
        paint.shader = shader
        canvas.drawCircle(cx, cy, r, paint)
        paint.shader = null; paint.style = Paint.Style.STROKE; paint.strokeWidth = 4f
        canvas.drawCircle(cx, cy, r, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            onDraw(canvas)
            val x = event.x.toInt().coerceIn(0, width - 1)
            val y = event.y.toInt().coerceIn(0, height - 1)
            val pixel = bmp.getPixel(x, y)
            onColorChanged?.invoke(pixel)
            bmp.recycle()
        }
        return true
    }
}

class ColorPickerActivity : AppCompatActivity() {

    private lateinit var colorPreview: View
    private lateinit var tvHex: TextView
    private lateinit var tvRGB: TextView
    private lateinit var tvCMYK: TextView
    private lateinit var colorWheel: ColorWheelView
    private lateinit var etSearch: EditText
    private lateinit var lvColors: ListView

    private val chineseColors = mutableListOf<ChineseColor>()
    private val filteredColors = mutableListOf<ChineseColor>()

    data class ChineseColor(val name: String, val hex: String, val rgb: String, val cmyk: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        colorPreview = findViewById(R.id.color_preview)
        tvHex = findViewById(R.id.tv_hex)
        tvRGB = findViewById(R.id.tv_rgb)
        tvCMYK = findViewById(R.id.tv_cmyk)
        colorWheel = findViewById(R.id.color_wheel)
        etSearch = findViewById(R.id.et_search)
        lvColors = findViewById(R.id.lv_colors)

        colorWheel.onColorChanged = { color ->
            colorPreview.setBackgroundColor(color)
            tvHex.text = String.format("#%06X", 0xFFFFFF and color)
            tvRGB.text = String.format("RGB(%d,%d,%d)", Color.red(color), Color.green(color), Color.blue(color))
            tvCMYK.text = "CMYK: -"
        }

        loadChineseColors()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filter(s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadChineseColors() {
        try {
            val json = assets.open("chinesecolors.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val hex = obj.getString("hex")
                val rgbArr = obj.getJSONArray("RGB")
                val cmykArr = obj.getJSONArray("CMYK")
                chineseColors.add(ChineseColor(
                    obj.getString("name"), hex,
                    "RGB(${rgbArr[0]},${rgbArr[1]},${rgbArr[2]})",
                    "CMYK(${cmykArr[0]},${cmykArr[1]},${cmykArr[2]},${cmykArr[3]})"
                ))
            }
            filteredColors.addAll(chineseColors)
            updateColorList()
        } catch (e: Exception) {
            Toast.makeText(this, "加载颜色数据失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filter(query: String) {
        filteredColors.clear()
        filteredColors.addAll(if (query.isBlank()) chineseColors else chineseColors.filter {
            it.name.contains(query) || it.hex.contains(query)
        })
        updateColorList()
    }

    private fun updateColorList() {
        val displayNames = filteredColors.map { "${it.name} (${it.hex})" }
        lvColors.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNames)
    }
}
