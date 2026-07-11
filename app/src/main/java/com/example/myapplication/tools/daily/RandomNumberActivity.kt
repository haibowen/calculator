package com.example.myapplication.tools.daily

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class RandomNumberActivity : AppCompatActivity() {

    private lateinit var etMin: EditText
    private lateinit var etMax: EditText
    private lateinit var cbUnique: CheckBox
    private lateinit var btnGenerate: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_number)

        etMin = findViewById(R.id.et_min)
        etMax = findViewById(R.id.et_max)
        cbUnique = findViewById(R.id.cb_unique)
        btnGenerate = findViewById(R.id.btn_generate)
        tvResult = findViewById(R.id.tv_result)

        btnGenerate.setOnClickListener { generate() }
    }

    private fun generate() {
        val min = etMin.text.toString().toIntOrNull() ?: return
        val max = etMax.text.toString().toIntOrNull() ?: return
        if (min >= max) {
            tvResult.text = "最大值需大于最小值"
            return
        }
        val result = if (cbUnique.isChecked) {
            (min..max).toList().shuffled().take(1).joinToString()
        } else {
            (min..max).random().toString()
        }
        tvResult.text = result
    }
}
