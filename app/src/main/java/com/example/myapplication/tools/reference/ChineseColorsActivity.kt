package com.example.myapplication.tools.reference

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import org.json.JSONArray

class ChineseColorsActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var gvColors: GridView
    private val allColors = mutableListOf<ColorItem>()
    private val displayColors = mutableListOf<ColorItem>()

    data class ColorItem(val name: String, val hex: String, val pinyin: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chinese_colors)

        etSearch = findViewById(R.id.et_search)
        gvColors = findViewById(R.id.gv_colors)

        loadColors()
        gvColors.adapter = ColorAdapter(displayColors)
        gvColors.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val c = displayColors[position]
            Toast.makeText(this, "${c.name} (${c.hex})", Toast.LENGTH_SHORT).show()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                displayColors.clear()
                displayColors.addAll(if (s.isNullOrBlank()) allColors else allColors.filter {
                    it.name.contains(s) || it.hex.contains(s) || it.pinyin.contains(s)
                })
                gvColors.adapter = ColorAdapter(displayColors)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadColors() {
        try {
            val json = assets.open("chinesecolors.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                allColors.add(ColorItem(
                    obj.getString("name"), obj.getString("hex"), obj.getString("pinyin")
                ))
            }
            displayColors.addAll(allColors)
        } catch (e: Exception) {
            Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class ColorAdapter(private val items: List<ColorItem>) : BaseAdapter() {
        override fun getCount() = items.size
        override fun getItem(pos: Int) = items[pos]
        override fun getItemId(pos: Int) = pos.toLong()

        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@ChineseColorsActivity)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            val item = items[pos]
            view.setBackgroundColor(Color.parseColor(item.hex))
            val tv = view.findViewById<TextView>(android.R.id.text1)
            tv.text = item.name
            tv.textSize = 10f
            tv.gravity = android.view.Gravity.CENTER
            return view
        }
    }
}
