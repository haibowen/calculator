package com.example.myapplication.tools.reference

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class TodayInHistoryActivity : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var lvEvents: ListView
    private val historyData = mutableMapOf<String, List<HistoryEvent>>()

    data class HistoryEvent(val year: String, val title: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_in_history)

        datePicker = findViewById(R.id.date_picker)
        lvEvents = findViewById(R.id.lv_events)

        loadTodayData()

        datePicker.init(datePicker.year, datePicker.month, datePicker.dayOfMonth) { _, _, _, _ ->
            loadEvents()
        }
    }

    private fun loadTodayData() {
        historyData["1-1"] = listOf(
            HistoryEvent("前45年", "罗马共和国开始使用儒略历"),
            HistoryEvent("1981年", "中国颁布《中华人民共和国婚姻法》"),
            HistoryEvent("2006年", "中国废止农业税"),
        )
        historyData["6-14"] = listOf(
            HistoryEvent("1777年", "美国采用星条旗作为国旗"),
            HistoryEvent("1905年", "爱因斯坦提出相对论"),
            HistoryEvent("1945年", "中共七大在延安开幕"),
        )
        historyData["10-1"] = listOf(
            HistoryEvent("1949年", "中华人民共和国成立"),
            HistoryEvent("1999年", "国庆50周年大阅兵"),
        )
        historyData["12-25"] = listOf(
            HistoryEvent("1991年", "苏联解体"),
            HistoryEvent("2000年", "圣诞节"),
        )
        historyData["7-1"] = listOf(
            HistoryEvent("1921年", "中国共产党成立"),
            HistoryEvent("1997年", "香港回归"),
        )
        historyData["8-15"] = listOf(
            HistoryEvent("1945年", "日本宣布无条件投降"),
        )
        loadEvents()
    }

    private fun loadEvents() {
        val key = "${datePicker.month + 1}-${datePicker.dayOfMonth}"
        val events = historyData[key] ?: listOf(HistoryEvent("暂无数据", "这一天暂无历史事件"))
        val displayItems = events.map { "${it.year} - ${it.title}" }
        lvEvents.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayItems)
    }
}
