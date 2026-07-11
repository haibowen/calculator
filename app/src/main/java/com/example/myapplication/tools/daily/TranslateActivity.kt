package com.example.myapplication.tools.daily

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class TranslateActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var etSource: EditText
    private lateinit var btnTranslate: Button
    private lateinit var tvResult: TextView

    private val languages = arrayOf(
        "自动检测", "中文", "英语", "日语", "韩语", "法语", "德语", "俄语", "西班牙语"
    )
    private val langCodes = arrayOf("", "zh", "en", "ja", "ko", "fr", "de", "ru", "es")
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        spinnerFrom = findViewById(R.id.spinner_from_lang)
        spinnerTo = findViewById(R.id.spinner_to_lang)
        etSource = findViewById(R.id.et_source)
        btnTranslate = findViewById(R.id.btn_translate)
        tvResult = findViewById(R.id.tv_result)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
        spinnerTo.setSelection(1)

        btnTranslate.setOnClickListener { doTranslate() }
    }

    private fun doTranslate() {
        val text = etSource.text.toString()
        if (text.isBlank()) return
        val from = langCodes[spinnerFrom.selectedItemPosition]
        val to = langCodes[spinnerTo.selectedItemPosition]
        tvResult.text = "翻译中..."

        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val url = "https://api.mymemory.translated.net/get?q=${java.net.URLEncoder.encode(text, "UTF-8")}&langpair=${from}|$to"
                    val json = URL(url).readText()
                    JSONObject(json).getJSONObject("responseData").getString("translatedText")
                }
                tvResult.text = result
            } catch (e: Exception) {
                tvResult.text = "翻译失败: ${e.message}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
