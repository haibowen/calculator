package com.example.myapplication.tools.daily

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.text.DecimalFormat

class UnitConverterActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var etInput: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvResult: TextView

    private val lengthUnits = arrayOf("毫米", "厘米", "分米", "米", "千米", "英寸", "英尺")
    private val weightUnits = arrayOf("毫克", "克", "千克", "吨", "磅", "盎司")
    private val tempUnits = arrayOf("摄氏度", "华氏度", "开尔文")
    private val areaUnits = arrayOf("平方毫米", "平方厘米", "平方米", "公顷", "平方千米")
    private val volumeUnits = arrayOf("毫升", "升", "立方米", "加仑")
    private val speedUnits = arrayOf("米/秒", "千米/时", "英里/时", "节")

    private val allUnits = arrayOf(lengthUnits, weightUnits, tempUnits, areaUnits, volumeUnits, speedUnits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_converter)

        spinnerCategory = findViewById(R.id.spinner_category)
        etInput = findViewById(R.id.et_input)
        spinnerFrom = findViewById(R.id.spinner_from)
        spinnerTo = findViewById(R.id.spinner_to)
        tvResult = findViewById(R.id.tv_result)

        val catAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, 
            resources.getStringArray(R.array.converter_categories))
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = catAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, pos: Int, id: Long) {
                val adapter = ArrayAdapter(this@UnitConverterActivity, android.R.layout.simple_spinner_item, allUnits[pos])
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrom.adapter = adapter
                spinnerTo.adapter = ArrayAdapter(this@UnitConverterActivity, android.R.layout.simple_spinner_item, allUnits[pos])
                convert()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        etInput.setOnEditorActionListener { _, _, _ -> convert(); false }

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, p2: Int, id: Long) { convert() }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, p2: Int, id: Long) { convert() }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private fun convert() {
        val input = etInput.text.toString().toDoubleOrNull() ?: return
        val cat = spinnerCategory.selectedItemPosition
        val fromIdx = spinnerFrom.selectedItemPosition
        val toIdx = spinnerTo.selectedItemPosition
        val result = when (cat) {
            0 -> convertLength(input, fromIdx, toIdx)
            1 -> convertWeight(input, fromIdx, toIdx)
            2 -> convertTemp(input, fromIdx, toIdx)
            3 -> convertArea(input, fromIdx, toIdx)
            4 -> convertVolume(input, fromIdx, toIdx)
            5 -> convertSpeed(input, fromIdx, toIdx)
            else -> input
        }
        tvResult.text = DecimalFormat("#.####").format(result)
    }

    private fun convertLength(v: Double, from: Int, to: Int): Double {
        val toMm = doubleArrayOf(1.0, 10.0, 100.0, 1000.0, 1e6, 25.4, 304.8)
        return v * toMm[from] / toMm[to]
    }

    private fun convertWeight(v: Double, from: Int, to: Int): Double {
        val toMg = doubleArrayOf(1.0, 1000.0, 1e6, 1e9, 453592.37, 28349.5)
        return v * toMg[from] / toMg[to]
    }

    private fun convertTemp(v: Double, from: Int, to: Int): Double {
        val inC = when (from) { 0 -> v; 1 -> (v - 32) * 5 / 9; else -> v - 273.15 }
        return when (to) { 0 -> inC; 1 -> inC * 9 / 5 + 32; else -> inC + 273.15 }
    }

    private fun convertArea(v: Double, from: Int, to: Int): Double {
        val toSqMm = doubleArrayOf(1.0, 100.0, 1e6, 1e10, 1e12)
        return v * toSqMm[from] / toSqMm[to]
    }

    private fun convertVolume(v: Double, from: Int, to: Int): Double {
        val toMl = doubleArrayOf(1.0, 1000.0, 1e6, 3785.41)
        return v * toMl[from] / toMl[to]
    }

    private fun convertSpeed(v: Double, from: Int, to: Int): Double {
        val toMps = doubleArrayOf(1.0, 0.277778, 0.44704, 0.514444)
        return v * toMps[from] / toMps[to]
    }
}
